package com.runemagic.gw2tools.api.character;

public enum CharacterRace
{
	ASURA("Asura"),
	CHARR("Charr"),
	HUMAN("Human"),
	NORN("Norn"),
	SYLVARI("Sylvari");

	private final String name;

	CharacterRace(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static CharacterRace byName(String name)
	{
		for (CharacterRace race:values())
		{
			if (race.getName().equals(name)) return race;
		}
		return null;
	}

	@Override public String toString()
	{
		return name;
	}
}
