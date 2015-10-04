package com.runemagic.gw2tools.api.account;

public enum GW2APIPermission
{
	ACCOUNT("account"),
	CHARACTERS("characters"),
	INVENTORIES("inventories"),
	TRADINGPOST("tradingpost"),
	WALLET("wallet"),
	UNLOCKS("unlocks"),
	PVP("pvp"),
	BUILDS("builds");

	private String name;

	GW2APIPermission(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	public static GW2APIPermission byName(String name)
	{
		for (GW2APIPermission val:values())
		{
			if (val.getName().equals(name)) return val;
		}
		return null;
	}

	@Override public String toString()
	{
		return name;
	}
}
