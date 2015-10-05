package com.runemagic.gw2tools.api.account;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONObject;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class World extends AbstractAPIObject
{
	private static final String API_RESOURCE_WORLDS="worlds";

	private IntegerProperty id=new SimpleIntegerProperty();
	private StringProperty name=new SimpleStringProperty();
	private StringProperty population=new SimpleStringProperty();

	private ObjectProperty<WorldRegion> region=new SimpleObjectProperty<>();
	private ObjectProperty<WorldLanguage> language=new SimpleObjectProperty<>();

	public World(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
		this.id.addListener((obs,ov,nv)->{
			region.setValue(WorldRegion.of((int)nv));
			language.setValue(WorldLanguage.of((int)nv));
		});
	}

	@Override protected void updateImpl() throws GW2APIException
	{
		JSONObject json = new JSONObject(readAPIv2Resource(API_RESOURCE_WORLDS+"/"+id.get()));
		name.set(json.getString("name"));
		population.set(json.getString("population"));//TODO consider using an enum
	}

	public int getId()
	{
		return id.get();
	}

	public IntegerProperty idProperty()
	{
		return id;
	}

	public String getName()
	{
		return name.get();
	}

	public ReadOnlyStringProperty nameProperty()
	{
		return name;
	}

	public String getPopulation()
	{
		return population.get();
	}

	public ReadOnlyStringProperty populationProperty()
	{
		return population;
	}

	public WorldRegion getRegion()
	{
		return region.get();
	}

	public ReadOnlyObjectProperty<WorldRegion> regionProperty()
	{
		return region;
	}

	public WorldLanguage getLanguage()
	{
		return language.get();
	}

	public ReadOnlyObjectProperty<WorldLanguage> languageProperty()
	{
		return language;
	}
}
