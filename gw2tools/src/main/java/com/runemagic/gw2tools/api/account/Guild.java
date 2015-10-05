package com.runemagic.gw2tools.api.account;

import java.util.Objects;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class Guild extends AbstractAPIObject
{
	private StringProperty id=new SimpleStringProperty();

	public Guild(GW2APISource source, String id)
	{
		super(source);
		this.id.set(id);
	}

	@Override
	protected void updateImpl() throws GW2APIException
	{
		//TODO needs v1 API
	}

	public String getID()
	{
		return id.get();
	}

	public ReadOnlyStringProperty idProperty()
	{
		return id;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Guild guild = (Guild) o;
		return Objects.equals(getID(), guild.getID());
	}

	@Override public int hashCode()
	{
		return Objects.hash(getID());
	}

	@Override public String toString()//TODO placeholder
	{
		return "Guild{" +
				"id=" + id +
				'}';
	}
}
