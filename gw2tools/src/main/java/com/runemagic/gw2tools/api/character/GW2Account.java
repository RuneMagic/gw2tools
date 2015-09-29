package com.runemagic.gw2tools.api.character;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import com.runemagic.gw2tools.api.APIKeyHolder;

public class GW2Account implements APIKeyHolder
{
	private ListProperty<GW2Character> characters=new SimpleListProperty<>(this, "characters", FXCollections.observableArrayList());
	private StringProperty apiKey=new SimpleStringProperty();

	public GW2Account(String apiKey)
	{
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
}
