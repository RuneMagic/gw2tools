package com.runemagic.gw2tools.api.account;

public enum WorldRegion
{
	NORTH_AMERICA(1, "North America", "NA"),
	EUROPE(2, "Europe", "EU");

	private int id;
	private String name;
	private String shortName;

	WorldRegion(int id, String name, String shortName)
	{
		this.id=id;
		this.name=name;
		this.shortName=shortName;
	}

	public static WorldRegion of(int worldID)
	{
		return byID(Integer.valueOf(String.valueOf(worldID).substring(0,1)));//TODO exception handling
	}

	public static WorldRegion byID(int id)
	{
		for (WorldRegion val:values())
		{
			if (val.getID()==id) return val;
		}
		return null;
	}

	public int getID()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getShortName()
	{
		return shortName;
	}

	@Override
	public String toString()
	{
		return shortName;
	}
}
