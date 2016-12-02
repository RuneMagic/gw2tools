package com.runemagic.gw2tools.raid;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public enum RaidWing
{
	SPIRIT_VALE("Spirit Vale", 0, 10),
	SALVATION_PASS("Salvation Pass", 1, 10),
	STRONGHOLD_OF_THE_FAITHFUL("Stronghold of the Faithful", 2, 10);

	private List<RaidBoss> bosses=null;
	private final String name;
	private final int number;
	private final int raidSize;

	private RaidWing(String name, int number, int raidSize)
	{
		this.name=name;
		this.number=number;
		this.raidSize=raidSize;
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

	public int getRaidSize()
	{
		return raidSize;
	}

	public String getName()
	{
		return name;
	}

	public RaidBoss getFirstBoss()
	{
		return getBosses().get(0);
	}

	public RaidBoss getLastBoss()
	{
		return getBosses().get(getBosses().size()-1);
	}

	public int getNumber()
	{
		return number;
	}
}
