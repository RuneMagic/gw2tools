package com.runemagic.gw2tools.api;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
	private static final String API_THREAD_POOL="GW2APIUpdate";

	private FloatProperty updateProgress=new SimpleFloatProperty();
	private BooleanProperty updating=new SimpleBooleanProperty();
	private BooleanProperty valid=new SimpleBooleanProperty();
	protected final GW2APISource source;
	//private Map<String, Property> fields=new HashMap<>();
	private Future<?> task;

	public AbstractAPIObject(GW2APISource source)
	{
		this.source=source;
	}

	protected abstract void updateImpl() throws GW2APIException;

	protected void onUpdateFinished(){}

	/*public ReadOnlyProperty<?> property(String name)
	{
		return fields.get(name);
	}

	public final Object get(String name)
	{
		ReadOnlyProperty<?> field=property(name);
		if (field==null) return null; //TODO exception/log
		return field.getValue();
	}*/

	protected String readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException
	{
		return source.readAPIv2Resource(resource, keyHolder);
	}

	protected String readAPIv2Resource(String resource) throws GW2APIException
	{
		return source.readAPIv2Resource(resource);
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
		ExecutorService exec=GW2Tools.inst().getThreadManager().getExecutor(API_THREAD_POOL);//TODO consider using javafx concurrency
		task=exec.submit(() -> {
			try
			{
				updateImpl();
				Platform.runLater(() -> valid.set(true));
			}
			catch (GW2APIException e)
			{
				e.printStackTrace();//TODO proper exception handling
				Platform.runLater(() -> valid.set(false));
			}
			finally
			{
				Platform.runLater(() -> {
					updating.set(false);
					updateProgress.set(1f);
					onUpdateFinished();
				});
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

	public void waitForUpdate()
	{
		if (task==null || task.isDone()) return;
		try
		{
			task.get();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();//TODO handle these properly
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
	}
}
