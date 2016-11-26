package com.runemagic.gw2tools.raid;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class CompositionBase
{
	private final List<RaidRole> roles;

	public CompositionBase(RaidRole... roles)
	{
		//TODO null check
		this.roles = ImmutableList.copyOf(roles);
	}

	public List<RaidRole> getRoles()
	{
		return roles;
	}

	public int getRaidSize()
	{
		return roles.size();
	}

}
