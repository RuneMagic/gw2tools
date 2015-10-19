package com.runemagic.gw2tools.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.account.GW2Account;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.api.account.TokenInfo;
import com.runemagic.gw2tools.api.account.World;
import com.runemagic.gw2tools.api.character.CharacterTrait;
import com.runemagic.gw2tools.api.character.GW2Character;
import com.runemagic.gw2tools.api.character.SpecializationInfo;
import com.runemagic.gw2tools.api.items.GW2Item;
import com.runemagic.gw2tools.util.GW2APIProfiler;

public class GW2API
{
	private static final GW2API instance=new GW2API();

	private final Map<String, Guild> guilds=new ConcurrentHashMap<>();
	private final Map<Integer, World> worlds=new ConcurrentHashMap<>();
	private final Map<Integer, GW2Item> items=new ConcurrentHashMap<>();
	private final Map<Integer, CharacterTrait> traits=new ConcurrentHashMap<>();
	private final Map<Integer, SpecializationInfo> specs=new ConcurrentHashMap<>();

	private final GW2APISource source;

	private GW2APIProfiler profiler;

	public GW2API()
	{
		profiler=new GW2APIProfiler();
		if (GW2Tools.inst().getAppSettings().sourceOptimizerEnabled.get())
			source=profiler.watchAPISource1(new GW2APISourceOptimizer(profiler.watchAPISource2(new DefaultGW2APISource())));
		else
			source=profiler.watchAPISource1(new DefaultGW2APISource());
	}

	public GW2APIProfiler getProfiler()
	{
		return profiler;
	}

	public static GW2API inst()
	{
		return instance;
	}

	public GW2APISource getSource()
	{
		return source;
	}

	public SpecializationInfo getSpecialization(Integer id)
	{
		if (id==null) return null;
		synchronized (specs)
		{
			SpecializationInfo ret = specs.get(id);
			//TODO validate specialization id
			if (ret == null)
			{
				ret = new SpecializationInfo(source, id);
				specs.put(id, ret);
				ret.update();//TODO better update schedule
			}
			return ret;
		}
	}

	public CharacterTrait getTrait(Integer id)
	{
		if (id==null) return null;
		synchronized (traits)
		{
			CharacterTrait ret = traits.get(id);
			//TODO validate trait id
			if (ret == null)
			{
				ret = new CharacterTrait(source, id);
				traits.put(id, ret);
				ret.update();//TODO better update schedule
			}
			return ret;
		}
	}

	public GW2Item getItem(Integer id)
	{
		if (id==null) return null;
		synchronized (items)
		{
			GW2Item ret = items.get(id);
			//TODO validate item id
			if (ret == null)
			{
				ret = new GW2Item(source, id);
				items.put(id, ret);
				ret.update();//TODO better update schedule
			}
			return ret;
		}
	}

	public World getWorld(Integer id)
	{
		if (id==null) return null;
		synchronized (worlds)
		{
			World ret = worlds.get(id);
			//TODO validate guild id
			if (ret == null)
			{
				ret = new World(source, id);
				worlds.put(id, ret);
				ret.update();//TODO better update schedule
			}
			return ret;
		}
	}

	public Guild getGuild(String id)
	{
		if (id==null || id.isEmpty()) return null;
		synchronized (guilds)
		{
			Guild ret = guilds.get(id);
			//TODO validate guild id
			if (ret == null)
			{
				ret = new Guild(source, id);
				guilds.put(id, ret);
				ret.update();//TODO better update schedule
			}
			return ret;
		}
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
