package com.runemagic.gw2tools.api.character;

public enum CraftingDiscipline
{
	ARTIFICER("Artificer"),
	ARMORSMITH("Armorsmith"),
	CHEF("Chef"),
	HUNTSMAN("Huntsman"),
	JEWELER("Jeweler"),
	LEATHERWORKER("Leatherworker"),
	TAILOR("Tailor"),
	WEAPONSMITH("Weaponsmith"),
	SCRIBE("Scribe");

	private final String name;

	CraftingDiscipline(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static CraftingDiscipline byName(String name)
	{
		for (CraftingDiscipline val:values())
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
