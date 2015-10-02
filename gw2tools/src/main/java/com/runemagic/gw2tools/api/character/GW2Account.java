package com.runemagic.gw2tools.api.character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import org.json.JSONArray;

import com.runemagic.gw2tools.api.APIKeyHolder;
import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class GW2Account extends AbstractAPIObject implements APIKeyHolder
{
	private final static String API_RESOURCE_CHARACTERS="characters";

	private ListProperty<GW2Character> characters=new SimpleListProperty<>(FXCollections.observableArrayList());
	private StringProperty apiKey=new SimpleStringProperty();
	//TODO more

	public GW2Account(GW2APISource source, String apiKey)
	{
		super(source);
		this.apiKey.set(apiKey);
	}

	public String getAPIKey()
	{
		return apiKey.get();
	}

	public ListProperty<GW2Character> getCharacters()
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
		List<String> charNames=new ArrayList<String>();
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
			GW2Character character=new GW2Character(source, charName, apiKey.get());
			characters.add(character);//TODO keep original order
			character.update();
		}

		for (GW2Character character:characters)
		{
			character.waitForUpdate();
		}
	}
}
