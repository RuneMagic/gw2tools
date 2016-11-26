package com.runemagic.gw2tools.raid;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RaidComposition
{
	private final Set<RaidSlot> slots;
	private final int hashcode;

	public RaidComposition(Collection<RaidSlot> slots)
	{
		this.slots=new HashSet<>(slots);
		hashcode=this.slots.hashCode();
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
