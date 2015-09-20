package com.runemagic.gw2tools.data;

public enum CharacterProfession
{
	ELEMENTALIST("Elementalist"),
	ENGINEER("Engineer"),
	GUARDIAN("Guardian"),
	MESMER("Mesmer"),
	NECROMANCER("Necromancer"),
	RANGER("Ranger"),
	REVENANT("Revenant"),
	THIEF("Thief"),
	WARRIOR("Warrior");

	private final String name;

	CharacterProfession(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static CharacterProfession byName(String name)
	{
		for (CharacterProfession val:values())
		{
			if (val.getName().equals(name)) return val;
		}
		return null;
	}
}
