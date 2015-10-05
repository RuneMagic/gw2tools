package com.runemagic.gw2tools.api;

import java.util.HashMap;
import java.util.Map;

import com.runemagic.gw2tools.api.account.GW2Account;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.api.account.TokenInfo;
import com.runemagic.gw2tools.api.account.World;
import com.runemagic.gw2tools.api.character.GW2Character;

public class GW2API
{
	private static final GW2API instance=new GW2API();

	private final Map<String, Guild> guilds=new HashMap<>();
	private final Map<Integer, World> worlds=new HashMap<>();

	private final GW2APISource source;

	public GW2API()
	{
		source=new DefaultGW2APISource();
	}

	public static GW2API inst()
	{
		return instance;
	}

	public World getWorld(int id)
	{
		World ret=worlds.get(id);
		//TODO validate guild id
		if (ret==null)
		{
			ret=new World(source, id);
			worlds.put(id, ret);
			ret.update();//TODO better update schedule
		}
		return ret;
	}

	public Guild getGuild(String id)
	{
		if (id==null) return null;
		Guild ret=guilds.get(id);
		//TODO validate guild id
		if (ret==null)
		{
			ret=new Guild(source, id);
			guilds.put(id, ret);
			ret.update();//TODO better update schedule
		}
		return ret;
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
