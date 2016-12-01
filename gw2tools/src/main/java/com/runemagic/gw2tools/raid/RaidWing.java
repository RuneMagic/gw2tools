package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public enum RaidWing
{
	SPIRIT_VALE,
	SALVATION_PASS,
	STRONGHOLD_OF_THE_FAITHFUL;

	private List<RaidBoss> bosses=null;

	private RaidWing()
	{

	}

	public List<RaidBoss> getBosses()
	{
		if (bosses==null)
		{
			List<RaidBoss> tmp=new ArrayList<RaidBoss>();
			for (RaidBoss boss:RaidBoss.values())
			{
				if (boss.getWing()==this) tmp.add(boss);
			}
			//Collections.sort(tmp);
			bosses=ImmutableList.copyOf(tmp);
		}
		return bosses;
	}

	public RaidBoss getFirstBoss()
	{
		return getBosses().get(0);
	}

	public RaidBoss getLastBoss()
	{
		return getBosses().get(getBosses().size()-1);
	}
}
