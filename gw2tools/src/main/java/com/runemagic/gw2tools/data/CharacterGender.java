package com.runemagic.gw2tools.data;

public enum CharacterGender
{
	MALE("Male"),
	FEMALE("Female");

	private final String name;

	CharacterGender(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static CharacterGender byName(String name)
	{
		for (CharacterGender val:values())
		{
			if (val.getName().equals(name)) return val;
		}
		return null;
	}
}
