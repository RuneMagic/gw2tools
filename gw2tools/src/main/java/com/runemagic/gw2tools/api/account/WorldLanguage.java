package com.runemagic.gw2tools.api.account;

public enum WorldLanguage
{
	ENGLISH(0, "English", "EN"),
	FRENCH(1, "French", "FR"),
	GERMAN(2, "German", "DE"),
	SPANISH(3, "Spanish", "SP");

	private int id;
	private String name;
	private String shortName;

	WorldLanguage(int id, String name, String shortName)
	{
		this.id=id;
		this.name=name;
		this.shortName=shortName;
	}

	public static WorldLanguage of(int worldID)
	{
		return byID(Integer.valueOf(String.valueOf(worldID).substring(1,2)));//TODO exception handling
	}

	public static WorldLanguage byID(int id)
	{
		for (WorldLanguage val:values())
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
