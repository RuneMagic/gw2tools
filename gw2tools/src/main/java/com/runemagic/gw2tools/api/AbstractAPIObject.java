package com.runemagic.gw2tools.api;

import java.lang.reflect.Field;
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
import javafx.collections.FXCollections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

	protected void addAPIv2Resource(String resourcePath, Consumer<JsonElement> resourceConsumer)
	{
		addAPIv2Resource(()->resourcePath, resourceConsumer);
	}

	protected void addAPIv2Resource(String resourcePath, APIKeyHolder keyHolder, Consumer<JsonElement> resourceConsumer)
	{
		addAPIv2Resource(() -> resourcePath, keyHolder, resourceConsumer);
	}

	protected void addAPIv1Resource(String resourcePath, String[] parameters, Consumer<JsonElement> resourceConsumer)
	{
		addAPIv1Resource(() -> resourcePath, () -> parameters, resourceConsumer);
	}

	protected void addAPIv1Resource(String resourcePath, Supplier<String[]> parametersSupplier, Consumer<JsonElement> resourceConsumer)
	{
		addAPIv1Resource(() -> resourcePath, parametersSupplier, resourceConsumer);
	}

	protected void addAPIv2Resource(Supplier<String> resourcePathSupplier, Consumer<JsonElement> resourceConsumer)
	{
		noInitCheck();
		resources.add(new ResourceProcessor(null, true, resourcePathSupplier, null, null, resourceConsumer));
	}

	protected void addAPIv2Resource(Supplier<String> resourcePathSupplier, APIKeyHolder keyHolder, Consumer<JsonElement> resourceConsumer)
	{
		noInitCheck();
		resources.add(new ResourceProcessor(null, true, resourcePathSupplier, null, keyHolder, resourceConsumer));
	}

	protected void addAPIv1Resource(Supplier<String> resourcePathSupplier, Supplier<String[]> parametersSupplier, Consumer<JsonElement> resourceConsumer)
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
			List<JsonElement> results=new ArrayList<>();
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
							JsonElement data=results.get(i);
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


	private final class FieldProcessor //TODO this is a mess
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
		public void process(JsonObject json) throws GW2APIException
		{
			GW2APIFieldType type=getSourceType();
			String name=field.name();
			Object val=null;
			if (!json.has(name))
			{
				if (!opt) throw new GW2APIException(AbstractAPIObject.this.getClass().getSimpleName()+": Missing required field ("+name+")");
			}
			else
			{
				val=getValue(json.get(name));
			}
			property.setValue(val);
		}

		private Object getValue(JsonElement json) throws GW2APIException
		{
			return getValue(json, getSourceType());
		}

		private Object getValue(JsonElement json, GW2APIFieldType type) throws GW2APIException
		{
			Object val;
			switch(type)
			{
			case STRING: val=json.getAsString(); break;
			case NUMBER: val=json.getAsInt(); break;
			case BOOLEAN: val=json.getAsBoolean(); break;
			case DATETIME: val=Instant.parse(json.getAsString()); break;
			case ARRAY: return getArray(json.getAsJsonArray());//don't process it
			case OBJECT: throw new UnsupportedOperationException("Object source type is not supported.");//TODO object source type
			// return getObject(json);
			case DEFAULT:
			default: throw new IllegalArgumentException("Unknown field source type: "+type);
			}
			return processValue(val);
		}

		private Object processValue(Object val) throws GW2APIException
		{
			if (factory!=null)
			{
				try
				{
					if (!field.targetType().equals(Object.class)) val = factory.invoke(null, val);
					else val = factory.invoke(GW2API.inst(), val);
				}
				catch (Throwable e)//TODO exception handling
				{
					throw new GW2APIException(AbstractAPIObject.this.getClass().getSimpleName()+": Factory method error ("+field.name()+")",e);
				}
			}
			return val;
		}

		private List<?> getArray(JsonArray json) throws GW2APIException//TODO return a read-only list
		{
			if (json==null) return null;
			GW2APIFieldType itemType=field.itemType();
			switch(itemType)
			{
			case DEFAULT: throw new IllegalArgumentException("Arrays can't have items of the 'default' type");
			case STRING:
				return getStringArray(json);
			case NUMBER:
				return getNumberArray(json);
			default: throw new UnsupportedOperationException(itemType+" arrays are not supported.");
			}
		}

		@SuppressWarnings("unchecked")
		private List<Object> getNumberArray(JsonArray json) throws GW2APIException
		{
			List<Object> list=FXCollections.observableArrayList();
			for (JsonElement loop:json)
			{
				list.add(processValue(loop.getAsInt()));
			}
			return list;
		}

		@SuppressWarnings("unchecked")
		private List<Object> getStringArray(JsonArray json) throws GW2APIException
		{
			List<Object> list=FXCollections.observableArrayList();
			for (JsonElement loop:json)
			{
				list.add(processValue(loop.getAsString()));
			}
			return list;
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
		private final Consumer<JsonElement> resourceConsumer;
		private final boolean v2;


		public ResourceProcessor(String name, boolean v2, Supplier<String> resource, Supplier<String[]> parameters, APIKeyHolder keyHolder, Consumer<JsonElement> resourceConsumer)
		{
			this.name = name;
			this.resource = resource;
			this.parameters = parameters;
			this.keyHolder = keyHolder;
			this.resourceConsumer = resourceConsumer;
			this.v2 = v2;
		}

		public JsonElement fetchData() throws GW2APIException
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

		public void process(JsonElement result)
		{
			if (result.isJsonObject())
			{
				for (FieldProcessor proc : fieldProcessors)
				{
					try
					{
						//TODO separate by API source
						proc.process((JsonObject)result);
					}
					catch (GW2APIException e)
					{
						//e.printStackTrace();
						//TODO feedback
					}
				}
			}

			if (resourceConsumer!=null) resourceConsumer.accept(result);
		}
	}
}
