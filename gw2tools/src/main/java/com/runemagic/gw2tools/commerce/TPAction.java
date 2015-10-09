package com.runemagic.gw2tools.commerce;

public enum TPAction
{
	BUY("Buy"),
	SELL("Sell"),
	FLIP("Flip");

	private final String name;

	TPAction(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return name;
	}

	@Override public String toString()
	{
		return name;
	}
}
