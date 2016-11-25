package com.runemagic.gw2tools.api.character;

import java.util.Arrays;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
	private IntegerProperty size=new SimpleIntegerProperty();
	private ListProperty<InventoryItem> items=new SimpleListProperty<>();

	public Inventory(int size)
	{
		this.size.set(size);
		items.set(FXCollections.observableList(Arrays.asList(new InventoryItem[size])));
	}

	public int getSize()
	{
		return size.get();
	}

	public ReadOnlyIntegerProperty sizeProperty()
	{
		return size;
	}

	public ObservableList<InventoryItem> getItems()
	{
		return items.get();
	}

}
