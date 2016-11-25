package com.runemagic.gw2tools.api.character;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.items.GW2Item;

public class InventoryItem
{

	private ObjectProperty<GW2Item> item=new SimpleObjectProperty<>();
	private IntegerProperty count=new SimpleIntegerProperty();

	public InventoryItem(int id, int count)
	{
		this.count.set(count);
		item.set(GW2API.inst().getItem(id));
	}

	public GW2Item getItem()
	{
		return item.get();
	}

	public ReadOnlyObjectProperty<GW2Item> itemProperty()
	{
		return item;
	}

	public int getCount()
	{
		return count.get();
	}

	public ReadOnlyIntegerProperty countProperty()
	{
		return count;
	}
}
