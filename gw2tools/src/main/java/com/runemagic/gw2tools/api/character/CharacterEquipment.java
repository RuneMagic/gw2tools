package com.runemagic.gw2tools.api.character;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class CharacterEquipment
{
	private ListProperty<EquipmentItem> items=new SimpleListProperty<>(FXCollections.observableArrayList());

}
