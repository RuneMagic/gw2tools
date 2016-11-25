package com.runemagic.gw2tools.api.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APIFieldType;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.character.CraftingDiscipline;

public class Recipe extends AbstractAPIObject
{
	private static final String API_RESOURCE_RECIPES="recipes";

	private IntegerProperty id=new SimpleIntegerProperty();

	@GW2APIField(name = "type")
	private StringProperty type=new SimpleStringProperty();//TODO enum

	@GW2APIField(name = "output_item_id")
	private IntegerProperty outputItemID=new SimpleIntegerProperty();

	@GW2APIField(name = "output_item_count")
	private IntegerProperty outputItemCount=new SimpleIntegerProperty();

	@GW2APIField(name = "time_to_craft_ms")
	private IntegerProperty timeToCraft=new SimpleIntegerProperty();

	@GW2APIField(name = "disciplines", sourceType = GW2APIFieldType.ARRAY, itemType = GW2APIFieldType.STRING, targetType = CraftingDiscipline.class, factory = "byName")
	private ListProperty<CraftingDiscipline> guilds=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list

	@GW2APIField(name = "min_rating")
	private IntegerProperty minRating=new SimpleIntegerProperty();

	//TODO flags

	private ListProperty<CraftingIngredient> ingredients=new SimpleListProperty<>(FXCollections.observableArrayList());//TODO unmodifiable list

	public Recipe(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(() -> API_RESOURCE_RECIPES + "/" + id.get(), this::updateRecipe);
	}

	private void updateRecipe(JsonElement data)
	{
		JsonArray ingredsArray=data.getAsJsonObject().get("ingredients").getAsJsonArray();
		ingredients.clear();
		for (JsonElement loop:ingredsArray)
		{
			JsonObject ingredJson=loop.getAsJsonObject();
			ingredients.add(new CraftingIngredient(ingredJson.get("item_id").getAsInt(), ingredJson.get("count").getAsInt()));
		}
	}



	public class CraftingIngredient
	{
		private final int itemID;
		private final int count;

		public CraftingIngredient(int itemID, int count)
		{
			this.itemID = itemID;
			this.count = count;
		}

		public int getItemID()
		{
			return itemID;
		}

		public int getCount()
		{
			return count;
		}
	}
}
