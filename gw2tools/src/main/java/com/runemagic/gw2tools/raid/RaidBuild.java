package com.runemagic.gw2tools.raid;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum RaidBuild
{
	ELEMENTALIST_STAFF(RaidRole.POWER_DAMAGE, RaidRole.STAFF_ELEMENTALIST),
	ELEMENTALIST_DAGGERWARHORN(RaidRole.POWER_DAMAGE),
	ENGINEER_POWER(RaidRole.POWER_DAMAGE),
	ENGINEER_CONDITION_DAMAGE(RaidRole.CONDITION_DAMAGE),
	GUARDIAN_HAMMER(RaidRole.POWER_DAMAGE, RaidRole.PROTECTION, RaidRole.HAMMER_DRAGONHUNTER),
	GUARDIAN_SCEPTERTORCH(RaidRole.POWER_DAMAGE),
	GUARDIAN_SWORDTORCH(RaidRole.POWER_DAMAGE),
	MESMER_TANK(RaidRole.CHRONOTANK),
	MESMER_CHRONOMANCER(RaidRole.CHRONOMANCER),
	MESMER_CONDITION_DAMAGE(RaidRole.CONDITION_DAMAGE),
	NECROMANCER_CONDITION_DAMAGE(RaidRole.CONDITION_DAMAGE, RaidRole.CONDITION_NECROMANCER),
	REVENANT_POWER(RaidRole.POWER_DAMAGE, RaidRole.PROTECTION, RaidRole.REVENANT),
	RANGER_POWER(RaidRole.HEALER),
	RANGER_CONDITION_DAMAGE(RaidRole.HEALER, RaidRole.CONDITION_DAMAGE),
	RANGER_HEALER(RaidRole.HEALER),
	THIEF_STAFF(RaidRole.POWER_DAMAGE),
	THIEF_DAGGER(RaidRole.POWER_DAMAGE),
	WARRIOR_POWER(RaidRole.PS_WARRIOR),
	WARRIOR_CONDITION_DAMAGE(RaidRole.PS_WARRIOR);

	private final Set<RaidRole> roles;
	private boolean power;

	private RaidBuild(RaidRole... roles)
	{
		this.roles = ImmutableSet.copyOf(roles);
		this.power=this.roles.contains(RaidRole.CONDITION_DAMAGE);
	}

	public boolean isPowerDamage()
	{
		return power;
	}

	public boolean isConditionDamage()
	{
		return !power;
	}

	public Set<RaidRole> getRoles()
	{
		return roles;
	}

	public boolean hasRole(RaidRole role)
	{
		return roles.contains(role);
	}
}
