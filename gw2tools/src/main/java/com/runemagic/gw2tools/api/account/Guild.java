package com.runemagic.gw2tools.api.account;

import java.util.Objects;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONObject;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APISource;

public class Guild extends AbstractAPIObject
{
	private final static String API_RESOURCE_GUILD="guild_details";

	private StringProperty id=new SimpleStringProperty();
	private StringProperty name=new SimpleStringProperty();
	private StringProperty tag=new SimpleStringProperty();
	//TODO emblem

	public Guild(GW2APISource source, String id)
	{
		super(source);
		this.id.set(id);
	}

	@Override
	protected void initResources()
	{
		addAPIv1Resource(API_RESOURCE_GUILD, ()->new String[]{"guild_id", id.get()}, this::updateGuild);
	}

	private void updateGuild(String data)
	{
		JSONObject json = new JSONObject(data);
		name.set(json.getString("guild_name"));
		tag.set(json.getString("tag"));
	}

	public String getID()
	{
		return id.get();
	}

	public ReadOnlyStringProperty idProperty()
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

	public String getTag()
	{
		return tag.get();
	}

	public ReadOnlyStringProperty tagProperty()
	{
		return tag;
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
		String name=getName();
		if (name==null) return getID();
		return name;
	}
}
