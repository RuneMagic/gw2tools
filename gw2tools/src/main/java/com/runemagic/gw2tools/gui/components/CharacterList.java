package com.runemagic.gw2tools.gui.components;

import javafx.scene.control.ListView;

public class CharacterList<GW2Character> extends ListView<GW2Character>
{

	public CharacterList()
	{
		initComponent();
	}

	private void initComponent()
	{
		setCellFactory(param -> new CharacterListCell());
	}

}
