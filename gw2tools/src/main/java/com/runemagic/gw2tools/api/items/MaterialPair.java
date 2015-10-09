package com.runemagic.gw2tools.api.items;

import com.runemagic.gw2tools.api.GW2API;

public class MaterialPair
{
	private final GW2Item rawMaterial;
	private final GW2Item processedMaterial;
	private final String name;
	private final int amount;

	public MaterialPair(String name, int amount, GW2Item raw, GW2Item processed)
	{
		this.name=name;
		this.amount=amount;
		rawMaterial=raw;
		processedMaterial=processed;
	}

	public MaterialPair(String name, int amount, int rawID, int processedID)
	{
		this.name=name;
		this.amount=amount;
		rawMaterial=GW2API.inst().getItem(rawID);
		processedMaterial=GW2API.inst().getItem(processedID);
	}

	public void update()
	{
		rawMaterial.update();
		processedMaterial.update();
	}

	public int getAmount()
	{
		return amount;
	}

	public GW2Item getRawMaterial()
	{
		return rawMaterial;
	}

	public GW2Item getProcessedMaterial()
	{
		return processedMaterial;
	}

	public String getName()
	{
		return name;
	}

	@Override public String toString()
	{
		return getName();
	}
}
