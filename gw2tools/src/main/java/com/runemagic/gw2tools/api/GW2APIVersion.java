package com.runemagic.gw2tools.api;


public enum GW2APIVersion
{
	V1("v1"),
	V2("v2");

	private final String name;

	GW2APIVersion(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static GW2APIVersion byName(String name)
	{
		for (GW2APIVersion val:values())
		{
			if (val.getName().equals(name)) return val;
		}
		return null;
	}

	@Override public String toString()
	{
		return name;
	}
}
