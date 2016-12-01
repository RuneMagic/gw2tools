package com.runemagic.gw2tools.raid;

public enum RaidBoss
{
	VALE_GUARDIAN("Vale Guardian", RaidWing.SPIRIT_VALE, 0, 2),
	GORSEVAL("Gorseval", RaidWing.SPIRIT_VALE, 1, 1),
	SABETHA("Sabetha", RaidWing.SPIRIT_VALE, 2, 3),

	SLOTHASOR("Slothasor", RaidWing.SALVATION_PASS, 0, 3),
	BANDIT_TRIO("Bandit Trio", RaidWing.SALVATION_PASS, 1, 2),
	MATTHIAS("Matthias", RaidWing.SALVATION_PASS, 2, 5),

	MCLEOD("Escort", RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 0, 1),
	KEEP_CONSTRUCT("Keep Construct", RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 1, 2),
	XERA("Xera", RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 2, 4);

	private final String name;
	private final RaidWing wing;
	private final int nth;
	private final int difficulty;

	private RaidBoss(String name, RaidWing wing, int nth, int difficulty)
	{
		this.name = name;
		this.wing = wing;
		this.nth = nth;
		this.difficulty = difficulty;
	}

	public RaidWing getWing()
	{
		return wing;
	}

	public int getNumber()
	{
		return nth;
	}

	public int getDifficulty()
	{
		return difficulty;
	}

	public String getName()
	{
		return name;
	}
}
