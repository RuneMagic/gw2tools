package com.runemagic.gw2tools.raid;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RaidMember
{
	private final Set<RaidRole> roles;
	private final Set<MemberBuild> builds;
	private final String name;

	public RaidMember(String name, Collection<MemberBuild> builds)
	{
		this.name=name;
		this.roles=new HashSet<>();
		this.builds=new HashSet<MemberBuild>(builds);
		updateRoles();
	}

	public RaidMember(String name, MemberBuild... builds)
	{
		this(name, Arrays.asList(builds));
	}

	public String getName()
	{
		return name;
	}

	private void updateRoles()
	{
		roles.clear();
		for (MemberBuild build:builds)
		{
			roles.addAll(build.getBuild().getRoles());
		}
	}

	public Set<RaidRole> getRoles()
	{
		return Collections.unmodifiableSet(roles);
	}

	public boolean hasRole(RaidRole role)
	{
		return roles.contains(role);
	}

	public int getRolePreference(RaidRole role)
	{
		int ret=0;
		for (MemberBuild build:builds)
		{
			if (!build.hasRole(role)) continue;
			int pref=build.getPreference();
			if (pref>ret) ret=pref;
		}
		return ret;
	}

	public int getRoleSkill(RaidRole role)
	{
		int ret=0;
		for (MemberBuild build:builds)
		{
			if (!build.hasRole(role)) continue;
			int skill=build.getSkill();
			if (skill>ret) ret=skill;
		}
		return ret;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RaidMember)) return false;

		RaidMember that = (RaidMember) o;

		return getName().equals(that.getName());

	}

	@Override public int hashCode()
	{
		return getName().hashCode();
	}

	@Override public String toString()
	{
		return "RaidMember{" +
				"name='" + name + '\'' +
				'}';
	}
}
