package com.runemagic.gw2tools.raid;

public enum RaidRole
{
	CHRONOTANK("Chronotank"),
	CHRONOMANCER("Chronomancer"),
	HEALER_DRUID("Healer Druid"),
	PROTECTION("Protection"),
	PS_WARRIOR("PS Warrior"),
	CONDI_WARRIOR("Condi PS Warrior"),
	CONDITION_DAMAGE("Condition DPS"),
	CONDITION_NECROMANCER("Condition Necromancer"),
	POWER_DAMAGE("Power DPS"),
	RANGED_DPS("Ranged Power DPS"),
	REVENANT("Revenant"),
	HAMMER_DRAGONHUNTER("Hammer Dragonhunter");

	private final String name;

	private RaidRole(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}
}
