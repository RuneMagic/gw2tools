package com.runemagic.gw2tools.raid;

public class MemberBuild
{
	private final RaidBuild build;
	private final int skill;
	private final int preference;

	public MemberBuild(RaidBuild build, int skill, int preference)
	{
		this.build = build;
		this.skill = skill;
		this.preference = preference;
	}

	public RaidBuild getBuild()
	{
		return build;
	}

	public int getSkill()
	{
		return skill;
	}

	public int getPreference()
	{
		return preference;
	}

	@Override public String toString()
	{
		return "MemberBuild{" +
				"build=" + build +
				", skill=" + skill +
				", preference=" + preference +
				'}';
	}

	public boolean hasRole(RaidRole role)
	{
		return build.hasRole(role);
	}
}
