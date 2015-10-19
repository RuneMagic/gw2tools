package com.runemagic.gw2tools.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.runemagic.gw2tools.GW2Tools;

public class GW2APISourceOptimizer implements GW2APISource
{
	private static final String API_BATCH_RESOURCE_THREAD_POOL="GW2APIBatchResource";
	private static final String API_BATCH_SCHEDULER_THREAD_POOL="GW2APIBatchScheduler";

	private static final long SCHEDULE_INTERVAL=10;

	private int maxBatchSize=100;
	private long batchTimeout=200;
	private List<APICallBatch> batches=new LinkedList<>();

	private final GW2APISource src;

	public GW2APISourceOptimizer(GW2APISource src)
	{
		this.src=src;
		loadCallBatches();
		startAPIBatchSchedulerThread();
	}

	private void loadCallBatches()
	{
		batches.add(createAPICallBatch("items"));
		batches.add(createAPICallBatch("commerce/prices"));
		batches.add(createAPICallBatch("traits"));
		batches.add(createAPICallBatch("specializations"));
		batches.add(createAPICallBatch("skins"));
	}

	private APICallBatch createAPICallBatch(String resource)
	{
		String base="https://api.guildwars2.com/v2/"+resource;
		return new APICallBatch(base, base+"?ids=", (s) -> {
			if (s.startsWith("/")) return new String[]{s.substring(1)};
			else if (s.startsWith("?ids=")) return s.substring(5).split(",");
			return null;
		});
	}

	private void startAPIBatchSchedulerThread()
	{
		ScheduledExecutorService exec=GW2Tools.inst().getThreadManager().getScheduledExecutor(API_BATCH_SCHEDULER_THREAD_POOL);
		exec.schedule(()->{
			for (APICallBatch batch:batches)
			{
				batch.onTick();
			}
		}, SCHEDULE_INTERVAL, TimeUnit.MILLISECONDS);
	}

	private APICallBatch getBatchFor(String request)
	{
		for (APICallBatch batch:batches)
		{
			if (batch.matches(request)) return batch;
		}
		return null;
	}

	private void executeRequest(APIResourceRequest req)
	{
		try
		{
			JsonElement res=src.readAPIv2Resource(req.getRequest());
			req.setResult(res);
		}
		catch (GW2APIException e)
		{
			req.setException(e);
		}
	}

	private void postRequest(APIResourceRequest req)
	{
		APICallBatch batch = getBatchFor(req.getRequest());
		if (batch == null)
		{
			executeRequest(req);
		}
		else
		{
			try
			{
				batch.addRequest(req);
			}
			catch (GW2APIException e)
			{
				executeRequest(req);
			}
		}
	}

	@Override public JsonElement readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException
	{
		//TODO handle requests that require API key
		return src.readAPIv2Resource(resource, keyHolder);
	}

	@Override public JsonElement readAPIv2Resource(String resource) throws GW2APIException
	{
		APIResourceRequest req = new APIResourceRequest(resource);
		postRequest(req);
		req.waitForResult();
		GW2APIException ex = req.getException();
		if (ex != null) throw ex;
		return req.getResult();
	}

	@Override public JsonElement readAPIv1Resource(String resource, String... parameters) throws GW2APIException
	{
		//TODO handle api v1 requests
		return src.readAPIv1Resource(resource, parameters);
	}

	private final class APIResourceRequest
	{
		private final String request;
		private final Object lock=new Object();
		private JsonElement result=null;
		private GW2APIException exception=null;
		private volatile boolean hasResult=false;
		private String[] ids;

		public APIResourceRequest(String request)
		{
			this.request = request;
		}

		public void waitForResult()
		{
			try
			{
				synchronized(lock)
				{
					while (!hasResult)
					{
						lock.wait();
					}
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				if (!hasResult) setException(new GW2APIException("Resource request thread interrupted without result", e));
			}
		}

		public void setResult(JsonElement result)
		{
			synchronized (lock)
			{
				this.result = result;
				hasResult=true;
				lock.notify();
			}
		}

		public String[] getIDs()
		{
			return ids;
		}

		public void setIDs(String[] ids)
		{
			this.ids = ids;
		}

		public JsonElement getResult()
		{
			return result;
		}

		public boolean hasResult()
		{
			return hasResult;
		}

		public String getRequest()
		{
			return request;
		}

		public void setException(GW2APIException exception)
		{
			synchronized (lock)
			{
				this.exception = exception;
				hasResult=true;
				lock.notify();
			}
		}

		public GW2APIException getException()
		{
			return exception;
		}
	}

	private final class APICallBatch
	{
		private final String pattern;
		private final String batchBase;
		private final Function<String, String[]> idExtractor;

		private final Object lock=new Object();

		private final List<APIResourceRequest> requests=new LinkedList<>();
		private final Set<String> ids=new HashSet<>();

		private volatile long lastProcessTime=0;

		public APICallBatch(String pattern, String batchBase, Function<String, String[]> idExtractor)
		{
			this.pattern=pattern;
			this.batchBase=batchBase;
			this.idExtractor=idExtractor;
		}

		public boolean matches(String request)
		{
			return request.startsWith(pattern);
		}

		private String build(List<String> ids)
		{
			if (ids.isEmpty()) throw new IllegalStateException("Trying to build from empty ID list");;
			return batchBase+String.join(",", ids);
		}

		public void process()
		{
			lastProcessTime=System.currentTimeMillis();
			List<String> ids;
			List<APIResourceRequest> requests;
			synchronized(lock)
			{
				if (this.requests.isEmpty()) return;
				if (this.ids.isEmpty()) throw new IllegalStateException("Requests without IDs");
				requests=new ArrayList<>(this.requests);
				ids=new ArrayList<>(this.ids);
				this.requests.clear();
				this.ids.clear();
			}

			ExecutorService exec=GW2Tools.inst().getThreadManager().getExecutor(API_BATCH_RESOURCE_THREAD_POOL);
			exec.submit(() -> {
				try
				{
					JsonElement res=src.readAPIv2Resource(build(ids));
					if (!res.isJsonArray()) throw new GW2APIException("The requested resource is not an array");//TODO this should be a runtime exception
					JsonArray arr = (JsonArray)res;
					if (arr.size()!=ids.size()) throw new GW2APIException("ID and result array size mismatch");
					Map<String,JsonElement> results=new HashMap<>();
					int n=0;
					for (JsonElement loop:arr)
					{
						results.put(ids.get(n), loop);
						n++;
					}
					//TODO handle "ids=all"
					for (APIResourceRequest req:requests)
					{
						for (String id:req.getIDs())
						{
							JsonElement data=results.get(id);
							if (data==null || data.isJsonNull()) req.setException(new GW2APIException("Failed to query item of id: "+id)); //TODO more descriptive exception
							else req.setResult(data);
						}
					}
				}
				catch (GW2APIException e)
				{
					for (APIResourceRequest req:requests)
					{
						req.setException(e);
					}
				}
			});
		}

		public void addRequest(APIResourceRequest req) throws GW2APIException
		{
			String[] extractedIDs=idExtractor.apply(req.getRequest().substring(pattern.length()));
			if (extractedIDs==null || extractedIDs.length==0)
			{
				throw new GW2APIException("Failed to parse the IDs: "+req.getRequest());
			}
			req.setIDs(extractedIDs);
			synchronized(lock)
			{
				Collections.addAll(ids, extractedIDs);
				requests.add(req);
				if (ids.size()>=maxBatchSize) process();
			}
		}

		public void onTick()
		{
			synchronized(lock)
			{
				if (requests.isEmpty()) return;
				if (System.currentTimeMillis()-lastProcessTime>SCHEDULE_INTERVAL) process();
			}
		}
	}
}
