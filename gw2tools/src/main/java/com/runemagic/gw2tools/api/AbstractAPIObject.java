package com.runemagic.gw2tools.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private List<FieldProcessor> fieldProcessors=new ArrayList<>();
	private boolean initialized;
	private boolean fieldsMapped;

	public AbstractAPIObject(GW2APISource source)
	{
		this.source=source;
		fieldsMapped=false;
		initialized=false;
		initResources();
		resources=Collections.unmodifiableList(resources);
		initialized=true;
	}

	private void parseFieldAnnotations()
	{
		fieldsMapped=true; //TODO more sophisticated way to avoid repeated failures
		for (Field field:this.getClass().getDeclaredFields())//TODO make this work with superclasses too
		{
			GW2APIField apiField=field.getAnnotation(GW2APIField.class);
			if (apiField==null) continue;
			try
			{
				field.setAccessible(true);
				Property prop=(Property)field.get(this);
				fieldProcessors.add(new FieldProcessor(apiField, prop));
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (GW2APIException e)
			{
				e.printStackTrace();//TODO proper exception handling
			}
		}
	}

	protected void onUpdateFinished(){}
	protected void onUpdateFailed(){}

	protected abstract void initResources();

	private void noInitCheck()
	{
		if (initialized) throw new IllegalStateException("Can't add resource processors after the object is initialized");
	}

	protected void addAPIv2Resource(String resourcePath, Consumer<String> resourceConsumer)
	{
		addAPIv2Resource(()->resourcePath, resourceConsumer);
	}

	protected void addAPIv2Resource(String resourcePath, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
	{
		addAPIv2Resource(() -> resourcePath, keyHolder, resourceConsumer);
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
		noInitCheck();
		resources.add(new ResourceProcessor(null, true, resourcePathSupplier, null, null, resourceConsumer));
	}

	protected void addAPIv2Resource(Supplier<String> resourcePathSupplier, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
	{
		noInitCheck();
		resources.add(new ResourceProcessor(null, true, resourcePathSupplier, null, keyHolder, resourceConsumer));
	}

	protected void addAPIv1Resource(Supplier<String> resourcePathSupplier, Supplier<String[]> parametersSupplier, Consumer<String> resourceConsumer)
	{
		noInitCheck();
		resources.add(new ResourceProcessor(null, false, resourcePathSupplier, parametersSupplier, null, resourceConsumer));
	}

	protected void progress(float increment)
	{
		updateProgress.set(updateProgress.get() + increment);
	}

	@Override
	public void update()
	{
		if (!fieldsMapped) parseFieldAnnotations();
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


	private static final class FieldProcessor //TODO this is a mess
	{
		private final GW2APIField field;
		private final Property property;
		private Method factory;
		private boolean opt;

		public FieldProcessor(GW2APIField field, Property property) throws GW2APIException
		{
			this.field = field;
			this.property = property;
			this.opt=field.optional();
			Class<?> targetClass;
			if (!field.targetType().equals(Object.class)) targetClass = field.targetType();
			else targetClass=GW2API.class;
			if (!field.factory().isEmpty())
			{
				GW2APIFieldType type=getSourceType();
				if (type==GW2APIFieldType.ARRAY) type=field.itemType();
				try
				{
					factory=targetClass.getMethod(field.factory(), type.getJavaClass());
				}
				catch (NoSuchMethodException e)
				{
					throw new GW2APIException(e);
				}
			}
			else factory=null;
		}

		@SuppressWarnings("unchecked")
		public void process(JSONObject json)
		{
			GW2APIFieldType type=getSourceType();
			String name=field.name();
			switch(type)
			{
			case STRING:
				property.setValue(getValue(json));
				break;
			case NUMBER:
				property.setValue(getValue(json));
				break;
			case BOOLEAN:
				property.setValue(getValue(json));
				break;
			case DATETIME:
				property.setValue(getValue(json));
				break;
			case ARRAY:
				processArray(getArray(json));
				break;
			case OBJECT:
				processObject(getObject(json));
				break;
			case DEFAULT:
			default: throw new IllegalArgumentException("Unknown field source type: "+type);
			}
		}

		private String getString(JSONObject json)
		{
			String name=field.name();
			return opt ? json.optString(name) : json.getString(name);
		}

		private Integer getNumber(JSONObject json)
		{
			String name=field.name();
			return opt ? json.optInt(name) : json.getInt(name);
		}

		private Boolean getBoolean(JSONObject json)
		{
			String name=field.name();
			return opt ? json.optBoolean(name) : json.getBoolean(name);
		}

		private Instant getDateTime(JSONObject json)
		{
			String name=field.name();
			String val=opt ? json.optString(name) : json.getString(name);
			if (val==null || val.isEmpty()) return null;
			return Instant.parse(val);
		}

		private JSONArray getArray(JSONObject json)
		{
			String name=field.name();
			return opt ? json.optJSONArray(name) : json.getJSONArray(name);
		}

		private JSONObject getObject(JSONObject json)
		{
			String name=field.name();
			return opt ? json.optJSONObject(name) : json.getJSONObject(name);
		}

		private Object getValue(JSONObject json)
		{
			return getValue(json, getSourceType());
		}

		private Object getValue(JSONObject json, GW2APIFieldType type)
		{
			Object val;
			switch(type)
			{
			case STRING: val=getString(json); break;
			case NUMBER: val=getNumber(json); break;
			case BOOLEAN: val=getBoolean(json); break;
			case DATETIME: val=getDateTime(json); break;
			case ARRAY: val=getArray(json); break;
			case OBJECT: throw new UnsupportedOperationException("Object source type is not supported.");//TODO object source type
			// return getObject(json);
			case DEFAULT:
			default: throw new IllegalArgumentException("Unknown field source type: "+type);
			}
			return processValue(val);
		}

		private Object processValue(Object val)
		{
			if (factory!=null)
			{
				try
				{
					if (!field.targetType().equals(Object.class)) val = factory.invoke(null, val);
					else val = factory.invoke(GW2API.inst(), val);
				}
				catch (IllegalAccessException | InvocationTargetException e)//TODO exception handling
				{
					e.printStackTrace();
				}
			}
			return val;
		}

		private void processObject(JSONObject json)
		{
			if (json==null) return;
			throw new UnsupportedOperationException("Object source type is not supported.");//TODO object source type
			/*try
			{
				Object val;
				if (!field.targetType().equals(Object.class)) val=factory.invoke(null, getValue(json));
				else val=factory.invoke(GW2API.inst(), getValue(json));

				property.setValue(val);
			}
			catch (IllegalAccessException | InvocationTargetException e)//TODO exception handling
			{
				e.printStackTrace();
			}*/
		}

		private void processArray(JSONArray json)
		{
			if (json==null) return;
			GW2APIFieldType itemType=field.itemType();
			switch(itemType)
			{
			case DEFAULT: throw new IllegalArgumentException("Arrays can't have items of the 'default' type");
			case STRING:
				processStringArray(json);
				break;
			case NUMBER:
				processNumberArray(json);
				break;
			case BOOLEAN: throw new UnsupportedOperationException("Arrays of booleans are not supported.");
			case DATETIME: throw new UnsupportedOperationException("Arrays of times are not supported.");
			case ARRAY: throw new UnsupportedOperationException("Arrays of arrays are not supported.");
			case OBJECT: throw new UnsupportedOperationException("Arrays of objects are not supported.");
			}
		}

		@SuppressWarnings("unchecked")
		private void processNumberArray(JSONArray json)
		{
			ListProperty list=(ListProperty)property;
			for (int i=0; i<json.length(); i++) list.add(processValue(json.getLong(i)));
		}

		@SuppressWarnings("unchecked")
		private void processStringArray(JSONArray json)
		{
			ListProperty list=(ListProperty)property;
			for (int i=0; i<json.length(); i++) list.add(processValue(json.getString(i)));
		}

		private GW2APIFieldType getSourceType()
		{
			GW2APIFieldType ret=field.sourceType();
			if (ret==GW2APIFieldType.DEFAULT)
			{
				if (property instanceof StringProperty) return GW2APIFieldType.STRING;
				if (property instanceof IntegerProperty || property instanceof LongProperty) return GW2APIFieldType.NUMBER;
				if (property instanceof BooleanProperty) return GW2APIFieldType.BOOLEAN;
				if (property instanceof ListProperty) return GW2APIFieldType.ARRAY;
				throw new IllegalArgumentException("Unknown property class: "+property.getClass());
			}
			else return ret;
		}
	}

	private final class ResourceProcessor
	{
		private final String name;
		private final Supplier<String> resource;
		private final Supplier<String[]> parameters;
		private final APIKeyHolder keyHolder;
		private final Consumer<String> resourceConsumer;
		private final boolean v2;


		public ResourceProcessor(String name, boolean v2, Supplier<String> resource, Supplier<String[]> parameters, APIKeyHolder keyHolder, Consumer<String> resourceConsumer)
		{
			this.name = name;
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
			try
			{
				JSONObject json=new JSONObject(result);
				for (FieldProcessor proc:fieldProcessors)
				{
					proc.process(json);
				}
			}
			catch (JSONException e)
			{
				//e.printStackTrace();
				//TODO proper JSON object/array validation
			}
			finally
			{
				if (resourceConsumer!=null) resourceConsumer.accept(result);
			}
		}
	}
}
