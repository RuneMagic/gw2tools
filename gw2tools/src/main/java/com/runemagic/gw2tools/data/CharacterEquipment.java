package com.runemagic.gw2tools.data;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class CharacterEquipment
{
	private ListProperty<EquipmentItem> items=new SimpleListProperty<>(FXCollections.observableArrayList());

}
