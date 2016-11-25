package com.runemagic.gw2tools.api.character;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.items.GW2Item;

public class InventoryBag
{

	private ObjectProperty<GW2Item> item=new SimpleObjectProperty<>();
	private ObjectProperty<Inventory> inventory=new SimpleObjectProperty<>();

	public InventoryBag(int id, int size)
	{
		item.set(GW2API.inst().getItem(id));
		inventory.set(new Inventory(size));
	}

	public Inventory getInventory()
	{
		return inventory.get();
	}

	public ReadOnlyObjectProperty<Inventory> inventoryProperty()
	{
		return inventory;
	}

	public GW2Item getItem()
	{
		return item.get();
	}

	public ReadOnlyObjectProperty<GW2Item> itemProperty()
	{
		return item;
	}
}
