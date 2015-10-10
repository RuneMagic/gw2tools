package com.runemagic.gw2tools.api.character;

import com.faelar.util.javafx.FontIcon;

public enum CharacterGender
{
	MALE("Male", FontIcon.MARS),
	FEMALE("Female", FontIcon.VENUS);

	private final String name;
	private final FontIcon icon;

	CharacterGender(String name, FontIcon icon)
	{
		this.name=name;
		this.icon=icon;
	}

	public String getName()
	{
		return name;
	}

	public FontIcon getIcon()
	{
		return icon;
	}

	public static CharacterGender byName(String name)
	{
		for (CharacterGender val:values())
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
