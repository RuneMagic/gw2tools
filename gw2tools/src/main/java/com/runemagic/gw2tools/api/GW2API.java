package com.runemagic.gw2tools.api;

import com.runemagic.gw2tools.api.character.GW2Account;
import com.runemagic.gw2tools.api.character.GW2Character;

public class GW2API
{
	private final static GW2API instance=new GW2API();

	private final GW2APISource source;

	public GW2API()
	{
		source=new DefaultGW2APISource();
	}

	public static GW2API inst()
	{
		return instance;
	}


	public GW2Account getAccount(String apiKey)
	{
		return getAccount(new APIKey(apiKey));
	}

	public GW2Account getAccount(APIKey apiKey)
	{
		GW2Account acc=new GW2Account(source, apiKey);
		acc.update();
		return acc;
	}

	public GW2Character getCharacter(String name, String apiKey)
	{
		return getCharacter(name, new APIKey(apiKey));
	}

	public GW2Character getCharacter(String name, APIKey apiKey)
	{
		GW2Character character=new GW2Character(source, name, apiKey);
		character.update();
		return character;
	}
}
