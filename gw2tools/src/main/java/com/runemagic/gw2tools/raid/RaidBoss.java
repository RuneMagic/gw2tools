package com.runemagic.gw2tools.raid;

public enum RaidBoss
{
	VALE_GUARDIAN(RaidWing.SPIRIT_VALE, 0),
	GORSEVAL(RaidWing.SPIRIT_VALE, 1),
	SABETHA(RaidWing.SPIRIT_VALE, 2),

	SLOTHASOR(RaidWing.SALVATION_PASS, 0),
	BANDIT_TRIO(RaidWing.SALVATION_PASS, 1),
	MATTHIAS(RaidWing.SALVATION_PASS, 2),

	MCLEOD(RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 0),
	KEEP_CONSTRUCT(RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 1),
	XERA(RaidWing.STRONGHOLD_OF_THE_FAITHFUL, 2);

	private final RaidWing wing;
	private final int nth;

	private RaidBoss(RaidWing wing, int nth)
	{
		this.wing = wing;
		this.nth = nth;
	}

	public RaidWing getWing()
	{
		return wing;
	}

	public int getNumber()
	{
		return nth;
	}
}
