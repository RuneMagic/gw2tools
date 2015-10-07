package com.runemagic.gw2tools.api.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APISource;

public class GW2Item extends AbstractAPIObject
{
	private static final String API_RESOURCE_ITEMS="items";
	private static final String API_RESOURCE_PRICES="commerce/prices";

	private IntegerProperty id=new SimpleIntegerProperty();

	//general
	private StringProperty name=new SimpleStringProperty();
	private StringProperty description=new SimpleStringProperty();
	private ObjectProperty type=new SimpleObjectProperty();
	private ObjectProperty rarity=new SimpleObjectProperty();
	private IntegerProperty level=new SimpleIntegerProperty();
	private IntegerProperty vendorValue=new SimpleIntegerProperty();
	private IntegerProperty defaultSkin=new SimpleIntegerProperty();
	private ListProperty flags=new SimpleListProperty();
	private ListProperty gameTypes=new SimpleListProperty();
	private ListProperty restrictions=new SimpleListProperty();
	//TODO icon

	//TP
	private IntegerProperty buyQuantity=new SimpleIntegerProperty();
	private IntegerProperty buyUnitPrice=new SimpleIntegerProperty();
	private IntegerProperty sellQuantity=new SimpleIntegerProperty();
	private IntegerProperty sellUnitPrice=new SimpleIntegerProperty();

	public GW2Item(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(() -> API_RESOURCE_ITEMS + "/" + id.get(), this::updateGeneral);
		addAPIv2Resource(() -> API_RESOURCE_PRICES + "/" + id.get(), this::updateTP);
	}

	private void updateGeneral(String data)
	{

	}

	private void updateTP(String data)
	{

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
