package com.runemagic.gw2tools.api.character;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class EquipmentItem
{
	private IntegerProperty id=new SimpleIntegerProperty();
	private ObjectProperty<EquipmentSlot> slot=new SimpleObjectProperty<>();
	private ListProperty<Integer> upgrades=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO upgrades
	private ListProperty<Integer> infusions=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO infusions
	private IntegerProperty skin=new SimpleIntegerProperty();
}
