package com.runemagic.gw2tools.raid;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class RaidSession
{
	private final Map<RaidBoss, RaidComposition> bossComps;

	public RaidSession(Map<RaidBoss, RaidComposition> bossComps)
	{
		this.bossComps = ImmutableMap.copyOf(bossComps);
	}

	public RaidComposition getComposition(RaidBoss boss)
	{
		return bossComps.get(boss);
	}

}
