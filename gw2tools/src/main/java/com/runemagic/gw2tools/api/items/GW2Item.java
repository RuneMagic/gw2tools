package com.runemagic.gw2tools.api.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GW2Item
{
	private IntegerProperty id=new SimpleIntegerProperty();

	//TP
	private IntegerProperty buyQuantity=new SimpleIntegerProperty();
	private IntegerProperty buyUnitPrice=new SimpleIntegerProperty();
	private IntegerProperty sellQuantity=new SimpleIntegerProperty();
	private IntegerProperty sellUnitPrice=new SimpleIntegerProperty();



	public GW2Item(int id)
	{
		this.id.set(id);
	}


	public int getId()
	{
		return id.get();
	}

	public ReadOnlyIntegerProperty idProperty()
	{
		return id;
	}

	/*public void setId(int id)
	{
		this.id.set(id);
	}*/

	public int getBuyQuantity()
	{
		return buyQuantity.get();
	}

	public IntegerProperty buyQuantityProperty()
	{
		return buyQuantity;
	}

	public void setBuyQuantity(int buyQuantity)
	{
		this.buyQuantity.set(buyQuantity);
	}

	public int getBuyUnitPrice()
	{
		return buyUnitPrice.get();
	}

	public IntegerProperty buyUnitPriceProperty()
	{
		return buyUnitPrice;
	}

	public void setBuyUnitPrice(int buyUnitPrice)
	{
		this.buyUnitPrice.set(buyUnitPrice);
	}

	public int getSellQuantity()
	{
		return sellQuantity.get();
	}

	public IntegerProperty sellQuantityProperty()
	{
		return sellQuantity;
	}

	public void setSellQuantity(int sellQuantity)
	{
		this.sellQuantity.set(sellQuantity);
	}

	public int getSellUnitPrice()
	{
		return sellUnitPrice.get();
	}

	public IntegerProperty sellUnitPriceProperty()
	{
		return sellUnitPrice;
	}

	public void setSellUnitPrice(int sellUnitPrice)
	{
		this.sellUnitPrice.set(sellUnitPrice);
	}
}
