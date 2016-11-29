package com.runemagic.gw2tools.raid;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum RaidWing
{
	SPIRIT_VALE(RaidBoss.VALE_GUARDIAN, RaidBoss.GORSEVAL, RaidBoss.SABETHA),
	SALVATION_PASS(RaidBoss.SLOTHASOR, RaidBoss.BANDIT_TRIO, RaidBoss.MATTHIAS),
	STRONGHOLD_OF_THE_FAITHFUL(RaidBoss.MCLEOD, RaidBoss.KEEP_CONSTRUCT, RaidBoss.XERA);

	private final List<RaidBoss> bosses;

	private RaidWing(RaidBoss... bosses)
	{
		this.bosses = ImmutableList.copyOf(bosses);
	}

	public List<RaidBoss> getBosses()
	{
		return bosses;
	}
}
