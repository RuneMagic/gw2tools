package com.runemagic.gw2tools.raid;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class RaidSession
{
	private final Map<RaidBoss, RaidComposition> bossComps;
	private final SessionBase base;
	private final int swaps;
	private Float avgPreference;

	public RaidSession(SessionBase base, Map<RaidBoss, RaidComposition> bossComps, int swaps)
	{
		this.bossComps = ImmutableMap.copyOf(bossComps);
		this.base=base;
		this.swaps=swaps;
	}

	public RaidComposition getComposition(RaidBoss boss)
	{
		return bossComps.get(boss);
	}

	public List<RaidBoss> getBosses()
	{
		return base.getBosses();
	}

	public int getSwaps()
	{
		return swaps;
	}

	public float getAveragePreference()
	{
		if (avgPreference==null)
		{
			float sum=0f;
			int n=0;
			for (RaidComposition comp : bossComps.values())
			{
				sum+=comp.getAveragePreference();
				n++;
			}
			avgPreference=sum/(float)n;
		}
		return avgPreference;
	}

	@Override public String toString()
	{
		return "RaidSession{" +
				"bossComps=" + bossComps +
				'}';
	}
}
