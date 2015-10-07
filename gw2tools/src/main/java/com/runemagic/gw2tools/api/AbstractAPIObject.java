package com.runemagic.gw2tools.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;

import com.runemagic.gw2tools.GW2Tools;

public abstract class AbstractAPIObject implements GW2APIObject
{
	private static final String API_RESOURCE_THREAD_POOL="GW2APIResource";

	private FloatProperty updateProgress=new SimpleFloatProperty();
	private BooleanProperty updating=new SimpleBooleanProperty();
	private BooleanProperty valid=new SimpleBooleanProperty();
	protected final GW2APISource source;
	//private Map<String, Property> fields=new HashMap<>();
	private List<ResourceProcessor> resources=new ArrayList<>();
	private boolean initialized;

	private final Object updateLock=new Object();

	public AbstractAPIObject(GW2APISource source)
	{
		this.source=source;
		initialized=false;
		initResources();
		resources=Collections.unmodifiableList(resources);
		initialized=true;

		updating.addListener((obs,ov,nv)->{
			synchronized(updateLock)
			{
				if (!nv) updateLock.notify();
			}
		});
	}

	protected void onUpdateFinished(){}
	protected void onUpdateFailed(){}

	protected abstract void initResources();

	private void initCheck()
	{
		if (initialized) throw new IllegalStateException("Can't add resource processors after the object is initialized");
	}

	protected void addAPIv2Resource(String resourcePath, Consumer<String> resourceConsumer)
	{
		addAPIv2Resource(()->resourcePath, resourceConsumer);
	}

	protected void addAPIv2Resource(String resourcePath, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
	{
		addAPIv2Resource(()->resourcePath, keyHolder, resourceConsumer);
	}

	protected void addAPIv1Resource(String resourcePath, String[] parameters, Consumer<String> resourceConsumer)
	{
		addAPIv1Resource(() -> resourcePath, () -> parameters, resourceConsumer);
	}

	protected void addAPIv1Resource(String resourcePath, Supplier<String[]> parametersSupplier, Consumer<String> resourceConsumer)
	{
		addAPIv1Resource(() -> resourcePath, parametersSupplier, resourceConsumer);
	}

	protected void addAPIv2Resource(Supplier<String> resourcePathSupplier, Consumer<String> resourceConsumer)
	{
		initCheck();
		resources.add(new ResourceProcessor(true, resourcePathSupplier, null, null, resourceConsumer));
	}

	protected void addAPIv2Resource(Supplier<String> resourcePathSupplier, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
	{
		initCheck();
		resources.add(new ResourceProcessor(true, resourcePathSupplier, null, keyHolder, resourceConsumer));
	}

	protected void addAPIv1Resource(Supplier<String> resourcePathSupplier, Supplier<String[]> parametersSupplier, Consumer<String> resourceConsumer)
	{
		initCheck();
		resources.add(new ResourceProcessor(false, resourcePathSupplier, parametersSupplier, null, resourceConsumer));
	}

	protected void progress(float increment)
	{
		updateProgress.set(updateProgress.get()+increment);
	}

	@Override
	public void update()
	{
		//if (source==null) throw new IllegalStateException("API Data Source is null");
		if (updating.get()) return;
		updating.set(true);
		updateProgress.set(0f);
		ExecutorService exec=GW2Tools.inst().getThreadManager().getExecutor(API_RESOURCE_THREAD_POOL); //TODO consider using javafx concurrency
		exec.submit(() -> {
			List<String> results=new ArrayList<>();
			try
			{
				for (ResourceProcessor res : resources)
				{
					results.add(res.fetchData());
				}
				Platform.runLater(() -> {
					try
					{
						int len=resources.size();
						for (int i=0; i<len; i++)
						{
							ResourceProcessor res=resources.get(i);
							String data=results.get(i);
							res.process(data);
						}
						onUpdateFinished();
						valid.set(true);
					}
					catch (Throwable t)
					{
						t.printStackTrace();//TODO proper exception handling
						valid.set(false);
					}
					finally
					{
						updateProgress.set(1f);
						updating.set(false);
					}
				});
			}
			catch (Throwable t)
			{
				t.printStackTrace();//TODO proper exception handling
				Platform.runLater(() -> {
					try
					{
						onUpdateFailed();
					}
					finally
					{
						updateProgress.set(1f);
						valid.set(false);
						updating.set(false);
					}
				});
				return;
			}

		});
	}

	public boolean isValid()
	{
		return valid.get();
	}

	public BooleanProperty validProperty()
	{
		return valid;
	}

	@Override
	public boolean isUpdating()
	{
		return updating.get();
	}

	@Override public float getUpdateProgress()
	{
		return updateProgress.get();
	}

	@Override public ReadOnlyFloatProperty updateProgressProperty()
	{
		return updateProgress;
	}

	@Override
	public ReadOnlyBooleanProperty updatingProperty()
	{
		return updating;
	}

	private final class ResourceProcessor
	{
		private final Supplier<String> resource;
		private final Supplier<String[]> parameters;
		private final APIKeyHolder keyHolder;
		private final Consumer<String> resourceConsumer;
		private final boolean v2;

		public ResourceProcessor(boolean v2, Supplier<String> resource, Supplier<String[]> parameters, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
		{
			this.resource = resource;
			this.parameters = parameters;
			this.keyHolder = keyHolder;
			this.resourceConsumer = resourceConsumer;
			this.v2 = v2;
		}

		public String fetchData() throws GW2APIException
		{
			String res=resource.get();
			if (res==null || res.isEmpty()) throw new GW2APIException("Null or empty resource path");
			if (v2)
			{
				if (keyHolder==null) return source.readAPIv2Resource(res);
				else return source.readAPIv2Resource(res, keyHolder);
			}
			else return source.readAPIv1Resource(res, parameters.get());
		}

		public void process(String result)
		{
			resourceConsumer.accept(result);
		}
	}
}
