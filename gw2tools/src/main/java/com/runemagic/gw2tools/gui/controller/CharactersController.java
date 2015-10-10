package com.runemagic.gw2tools.gui.controller;

import javafx.fxml.FXML;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.character.GW2Character;
import com.runemagic.gw2tools.gui.components.CharacterList;

public class CharactersController
{
	@FXML private CharacterList<GW2Character> tblCharacters;


	public void initialize()
	{
		GW2Tools.inst().accountProperty().addListener((obs, oldVal, newVal) -> {
			tblCharacters.setItems(newVal.getCharacters());
		});
	}
}
