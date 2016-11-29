package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SessionBase
{
	private final Map<RaidBoss, CompositionBase> comps;
	private final List<RaidBoss> raidBosses;

	public SessionBase(Map<RaidBoss, CompositionBase> comps, RaidWing... raidWings)
	{
		this.comps = ImmutableMap.copyOf(comps);
		this.raidBosses = ImmutableList.copyOf(listBosses(raidWings));
		//TODO verify bosses
	}

	private List<RaidBoss> listBosses(RaidWing... raidWings)
	{
		List<RaidBoss> ret=new ArrayList<>();
		for (RaidWing wing:raidWings)
		{
			for (RaidBoss boss:wing.getBosses())
			{
				ret.add(boss);
			}
		}
		return ret;
	}

	public List<RaidBoss> getBosses()
	{
		return raidBosses;
	}

	public CompositionBase getCompositionBase(RaidBoss boss)
	{
		return comps.get(boss);
	}
}
