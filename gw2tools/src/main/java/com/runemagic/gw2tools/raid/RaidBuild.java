package com.runemagic.gw2tools.raid;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.runemagic.gw2tools.api.character.CharacterProfession;

public class RaidBuild
{
	private final Set<RaidRole> roles;
	private final String name;
	private final CharacterProfession prof;

	public RaidBuild(String name, CharacterProfession prof, Collection<RaidRole> roles)
	{
		this.name=name;
		this.prof=prof;
		this.roles = ImmutableSet.copyOf(roles);
	}

	public CharacterProfession getProfession()
	{
		return prof;
	}

	public String getName()
	{
		return name;
	}

	public Set<RaidRole> getRoles()
	{
		return roles;
	}

	public boolean hasRole(RaidRole role)
	{
		return roles.contains(role);
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RaidBuild)) return false;

		RaidBuild raidBuild = (RaidBuild) o;

		return getName().equals(raidBuild.getName());

	}

	@Override public int hashCode()
	{
		return getName().hashCode();
	}

	@Override public String toString()
	{
		return "RaidBuild{" +
				"roles=" + roles +
				", name='" + name + '\'' +
				", prof=" + prof +
				'}';
	}
}
