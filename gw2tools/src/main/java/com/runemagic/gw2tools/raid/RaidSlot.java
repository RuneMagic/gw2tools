package com.runemagic.gw2tools.raid;

public class RaidSlot
{
	private final RaidRole role;
	private final RaidMember member;
	private final int hashcode;

	public RaidSlot(RaidRole role, RaidMember member)
	{
		this.role = role;
		this.member = member;
		this.hashcode=calculateHashCode();
	}

	public RaidRole getRole()
	{
		return role;
	}

	public RaidMember getMember()
	{
		return member;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RaidSlot)) return false;

		RaidSlot raidSlot = (RaidSlot) o;

		if (!getRole().equals(raidSlot.getRole())) return false;
		return getMember().equals(raidSlot.getMember());

	}

	protected int calculateHashCode()
	{
		int result = getRole().hashCode();
		result = 31*result * getMember().hashCode();
		return result;
	}

	@Override public int hashCode()
	{
		return hashcode;
	}

	@Override public String toString()
	{
		return "RaidSlot{" +
				"role=" + role +
				", member=" + member +
				'}';
	}
}
