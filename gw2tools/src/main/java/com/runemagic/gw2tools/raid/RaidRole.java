package com.runemagic.gw2tools.raid;

public class RaidRole
{
	private final String name;
	private final String shortName;

	public RaidRole(String name, String shortName)
	{
		this.name=name;
		this.shortName=(shortName!=null && !shortName.isEmpty()?shortName:name);
	}

	public String getName()
	{
		return name;
	}

	public String getShortName()
	{
		return shortName;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RaidRole)) return false;

		RaidRole raidRole = (RaidRole) o;

		return getName().equals(raidRole.getName());
	}

	@Override public int hashCode()
	{
		return getName().hashCode();
	}

	@Override public String toString()
	{
		return "RaidRole{" +
				"name='" + name + '\'' +
				", shortName='" + shortName + '\'' +
				'}';
	}
}
