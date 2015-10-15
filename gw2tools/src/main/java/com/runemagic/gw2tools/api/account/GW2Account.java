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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.AuthenticatedAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APIFieldType;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.character.GW2Character;

public class GW2Account extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_CHARACTERS="characters";
	private final static String API_RESOURCE_ACCOUNT="account";

	private ListProperty<GW2Character> characters=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list

	@GW2APIField(name = "id")
	private StringProperty id=new SimpleStringProperty();

	@GW2APIField(name = "name")
	private StringProperty name=new SimpleStringProperty();

	@GW2APIField(name = "world", sourceType = GW2APIFieldType.NUMBER, factory = "getWorld")
	private ObjectProperty<World> world=new SimpleObjectProperty<>();

	@GW2APIField(name = "guilds", sourceType = GW2APIFieldType.ARRAY, itemType = GW2APIFieldType.STRING, factory = "getGuild")
	private ListProperty<Guild> guilds=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list

	@GW2APIField(name = "created", sourceType = GW2APIFieldType.DATETIME)
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

	@Override
	protected void initResources()
	{
		addAPIv2Resource(API_RESOURCE_ACCOUNT, this, null);
		addAPIv2Resource(API_RESOURCE_CHARACTERS, this, this::updateCharacters);
	}

	private void updateCharacters(JsonElement data)
	{
		if (!data.isJsonArray()) throw new IllegalArgumentException(); //TODO proper exception
		JsonArray json=(JsonArray)data;
		List<String> charNames=new ArrayList<>();
		for (JsonElement loop:json)
		{
			charNames.add(loop.getAsString());
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
			GW2Character character = new GW2Character(source, charName, getAPIKey());
			characters.add(character);//TODO keep original order
			character.update();//TODO update progress
		}
		/*float inc=1f/characters.size();
		for (GW2Character character:characters)
		{
			character.waitForUpdate();
			progress(inc);
		}*/
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
