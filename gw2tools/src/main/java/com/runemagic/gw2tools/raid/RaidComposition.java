package com.runemagic.gw2tools.raid;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class RaidComposition
{
	private final Set<RaidSlot> slots;
	private final int hashcode;
	private Integer minimumSkill=null;
	private Float averageSkill=null;
	private Integer minimumPreference=null;
	private Float averagePreference=null;
	//private Boolean perfectSkill=null;
	//private Boolean perfectPreference=null;

	public RaidComposition(Collection<RaidSlot> slots)
	{
		this.slots=ImmutableSet.copyOf(slots);
		hashcode=this.slots.hashCode();
	}

	public Set<RaidSlot> getSlots()
	{
		return slots;
	}

	/*public boolean getPerfectSkill()
	{
		if (perfectSkill==null) perfectSkill=calculatePerfectSkill();
		return perfectSkill;
	}

	private boolean calculatePerfectSkill()
	{
		return true;
	}

	public boolean getPerfectPreference()
	{
		if (perfectPreference==null) perfectPreference=calculatePerfectPreference();
		return perfectPreference;
	}

	private boolean calculatePerfectPreference()
	{
		for (RaidSlot slot:slots)
		{
			int pref=slot.getMember().getRolePreference(slot.getRole());

		}
	}*/

	private int calculateMinimumSkill()
	{
		int ret=5;
		for (RaidSlot slot:slots)
		{
			int skill=slot.getMember().getRoleSkill(slot.getRole());
			if (skill<ret) ret=skill;
		}
		return ret;
	}

	private float calculateAverageSkill()
	{
		float sum=0;
		int n=0;
		for (RaidSlot slot:slots)
		{
			sum+=slot.getMember().getRoleSkill(slot.getRole());
			n++;
		}
		return sum/(float)n;
	}

	private int calculateMinimumPreference()
	{
		int ret=5;
		for (RaidSlot slot:slots)
		{
			int pref=slot.getMember().getRolePreference(slot.getRole());
			if (pref<ret) ret=pref;
		}
		return ret;
	}

	private float calculateAveragePreference()
	{
		float sum=0;
		int n=0;
		for (RaidSlot slot:slots)
		{
			sum+=slot.getMember().getRolePreference(slot.getRole());
			n++;
		}
		return sum/(float)n;
	}

	public int getMinimumSkill()
	{
		if (minimumSkill==null) minimumSkill=calculateMinimumSkill();
		return minimumSkill;
	}

	public float getAverageSkill()
	{
		if (averageSkill==null) averageSkill=calculateAverageSkill();
		return averageSkill;
	}

	public int getMinimumPreference()
	{
		if (minimumPreference==null) minimumPreference=calculateMinimumPreference();
		return minimumPreference;
	}

	public float getAveragePreference()
	{
		if (averagePreference==null) averagePreference=calculateAveragePreference();
		return averagePreference;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RaidComposition)) return false;

		RaidComposition that = (RaidComposition) o;

		return slots.equals(that.slots);
	}

	@Override public int hashCode()
	{
		return hashcode;
	}

	@Override public String toString()
	{
		return "RaidComposition{" +
				"slots=" + slots +
				'}';
	}
}
