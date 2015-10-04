package com.runemagic.gw2tools.api;

import com.runemagic.gw2tools.api.account.GW2Account;
import com.runemagic.gw2tools.api.account.TokenInfo;
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

	public TokenInfo getTokenInfo(String apiKey) throws GW2APIException
	{
		return getTokenInfo(APIKey.of(apiKey));
	}

	public TokenInfo getTokenInfo(APIKey apiKey)
	{
		return update(new TokenInfo(source, apiKey));
	}

	public GW2Account getAccount(String apiKey) throws GW2APIException
	{
		return getAccount(APIKey.of(apiKey));
	}

	public GW2Account getAccount(APIKey apiKey)
	{
		return update(new GW2Account(source, apiKey));
	}

	public GW2Character getCharacter(String name, String apiKey) throws GW2APIException
	{
		return getCharacter(name, APIKey.of(apiKey));
	}

	public GW2Character getCharacter(String name, APIKey apiKey)
	{
		return update(new GW2Character(source, name, apiKey));
	}

	private <T extends GW2APIObject> T update(T obj)
	{
		obj.update();
		return obj;
	}
}
