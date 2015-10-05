package com.runemagic.gw2tools.api.account;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class World extends AbstractAPIObject
{

	private IntegerProperty id=new SimpleIntegerProperty();
	private StringProperty name=new SimpleStringProperty();
	private StringProperty population=new SimpleStringProperty();

	public World(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
	}

	@Override protected void updateImpl() throws GW2APIException
	{

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

	public StringProperty nameProperty()
	{
		return name;
	}

	public String getPopulation()
	{
		return population.get();
	}

	public StringProperty populationProperty()
	{
		return population;
	}
}
