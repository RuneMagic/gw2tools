package com.runemagic.gw2tools.api.character;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.items.GW2Item;

public class EquipmentItem extends GW2Item
{
	private ObjectProperty<EquipmentSlot> slot=new SimpleObjectProperty<>();
	private ListProperty<Integer> upgrades=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO upgrades
	private ListProperty<Integer> infusions=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO infusions
	private IntegerProperty skin=new SimpleIntegerProperty();

	public EquipmentItem(GW2APISource source, int id)
	{
		super(source, id);
	}

}
