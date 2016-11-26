package com.runemagic.gw2tools.raid;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class SessionBase
{
	private final Map<RaidBoss, CompositionBase> comps;

	public SessionBase(Map<RaidBoss, CompositionBase> comps)
	{
		//TODO null check
		this.comps = ImmutableMap.copyOf(comps);
	}

	public CompositionBase getCompositionBase(RaidBoss boss)
	{
		return comps.get(boss);
	}
}
