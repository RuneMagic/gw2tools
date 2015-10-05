package com.runemagic.gw2tools.api.account;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import org.json.JSONArray;

import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.AuthenticatedAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.character.GW2Character;

public class GW2Account extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_CHARACTERS="characters";

	private ListProperty<GW2Character> characters=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list
	private StringProperty id=new SimpleStringProperty();
	private StringProperty name=new SimpleStringProperty();
	private ObjectProperty<World> world=new SimpleObjectProperty<>();
	private ListProperty<Guild> guilds=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list
	private ObjectProperty<Instant> created=new SimpleObjectProperty<>();

	public GW2Account(GW2APISource source, APIKey apiKey)
	{
		super(source, apiKey);
	}

	public ReadOnlyListProperty<GW2Character> getCharacters()
	{
		return characters;
	}

	public GW2Character getCharacter(String name)
	{
		for (GW2Character character:characters)
		{
			if (character.getName().equals(name)) return character;
		}
		return null;
	}

	@Override protected void updateImpl() throws GW2APIException
	{
		List<String> charNames=new ArrayList<>();
		JSONArray json = new JSONArray(readAPIv2Resource(API_RESOURCE_CHARACTERS, this));
		int len=json.length();
		for (int i=0;i<len;i++)
		{
			charNames.add(json.getString(i));
		}

		Iterator<GW2Character> iter=characters.iterator();
		while (iter.hasNext())
		{
			GW2Character character=iter.next();
			if (charNames.remove(character.getName()))
			{
				character.update();
			}
			else
			{
				iter.remove();
			}
		}

		for (String charName:charNames)
		{
			GW2Character character=new GW2Character(source, charName, getAPIKey());
			characters.add(character);//TODO keep original order
			character.update();
		}
		float inc=1f/characters.size();
		for (GW2Character character:characters)
		{
			character.waitForUpdate();
			progress(inc);
		}

		//TODO the rest
	}

	public String getId()
	{
		return id.get();
	}

	public StringProperty idProperty()
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

	public World getWorld()
	{
		return world.get();
	}

	public ObjectProperty<World> worldProperty()
	{
		return world;
	}

	public ReadOnlyListProperty<Guild> getGuilds()
	{
		return guilds;
	}

	public Instant getCreated()
	{
		return created.get();
	}

	public ReadOnlyObjectProperty<Instant> createdProperty()
	{
		return created;
	}
}
