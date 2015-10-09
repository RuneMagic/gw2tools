package com.runemagic.gw2tools.api.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONObject;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APISource;

public class GW2Item extends AbstractAPIObject
{
	private static final String API_RESOURCE_ITEMS="items";
	private static final String API_RESOURCE_PRICES="commerce/prices";

	//@GW2APIField(name = "id")
	private IntegerProperty id=new SimpleIntegerProperty();

	//general
	@GW2APIField(name = "name")
	private StringProperty name=new SimpleStringProperty();

	@GW2APIField(name = "description")
	private StringProperty description=new SimpleStringProperty();


	private ObjectProperty type=new SimpleObjectProperty();
	private ObjectProperty rarity=new SimpleObjectProperty();

	@GW2APIField(name = "level")
	private IntegerProperty level=new SimpleIntegerProperty();

	@GW2APIField(name = "vendor_value")
	private IntegerProperty vendorValue=new SimpleIntegerProperty();

	@GW2APIField(name = "default_skin")
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
		JSONObject json=new JSONObject(data);
		JSONObject buys=json.getJSONObject("buys");
		buyQuantity.set(buys.getInt("quantity"));
		buyUnitPrice.set(buys.getInt("unit_price"));
		JSONObject sells=json.getJSONObject("sells");
		sellQuantity.set(sells.getInt("quantity"));
		sellUnitPrice.set(sells.getInt("unit_price"));
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

	public ReadOnlyIntegerProperty buyQuantityProperty()
	{
		return buyQuantity;
	}

	public int getBuyUnitPrice()
	{
		return buyUnitPrice.get();
	}

	public ReadOnlyIntegerProperty buyUnitPriceProperty()
	{
		return buyUnitPrice;
	}

	public int getSellQuantity()
	{
		return sellQuantity.get();
	}

	public ReadOnlyIntegerProperty sellQuantityProperty()
	{
		return sellQuantity;
	}

	public int getSellUnitPrice()
	{
		return sellUnitPrice.get();
	}

	public ReadOnlyIntegerProperty sellUnitPriceProperty()
	{
		return sellUnitPrice;
	}

	public String getDescription()
	{
		return description.get();
	}

	public ReadOnlyStringProperty descriptionProperty()
	{
		return description;
	}

	public String getName()
	{
		return name.get();
	}

	public ReadOnlyStringProperty nameProperty()
	{
		return name;
	}

	public int getLevel()
	{
		return level.get();
	}

	public ReadOnlyIntegerProperty levelProperty()
	{
		return level;
	}

	public int getVendorValue()
	{
		return vendorValue.get();
	}

	public ReadOnlyIntegerProperty vendorValueProperty()
	{
		return vendorValue;
	}
}
