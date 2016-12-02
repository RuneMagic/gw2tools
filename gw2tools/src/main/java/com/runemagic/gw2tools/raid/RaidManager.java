package com.runemagic.gw2tools.raid;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaidManager
{
	private static final Logger log = LoggerFactory.getLogger(RaidManager.class);

	private final RaidDataInterface loader;

	private final Map<String, RaidMember> members=new HashMap<>();
	private final Map<String, RaidBuild> builds=new HashMap<>();
	private final Map<String, RaidRole> roles=new HashMap<>();
	private SessionBase sessionBase;

	public RaidManager(RaidDataInterface loader)
	{
		this.loader=loader;
	}

	public void loadData()
	{
		members.clear();
		builds.clear();
		roles.clear();
		sessionBase=null;
		try
		{
			log.debug("Loading roles");
			for (RaidRole role:loader.getRoles())
			{
				roles.put(role.getName(), role);
			}
			log.debug("Loaded {} roles", roles.size());

			log.debug("Loading builds");
			for (RaidBuild build:loader.getBuilds())
			{
				builds.put(build.getName(), build);
			}
			log.debug("Loaded {} builds", builds.size());

			log.debug("Loading members");
			for (RaidMember member:loader.getMembers())
			{
				members.put(member.getName(), member);
			}
			log.debug("Loaded {} members", members.size());

			log.debug("Loading session base");
			sessionBase=loader.getSessionBase();
			log.debug("Loaded session base");
		}
		catch (RaidInterfaceException e)
		{
			e.printStackTrace();
		}
	}

	public SessionBase getSessionBase()
	{
		return sessionBase;
	}

	public SessionBuilder getSessionBuilder()
	{
		return new SessionBuilder(sessionBase, getMembers());
	}

	public RaidRole getRole(String name)
	{
		return roles.get(name);
	}

	public Collection<RaidRole> getRoles()
	{
		return Collections.unmodifiableCollection(roles.values());
	}

	public RaidBuild getBuild(String name)
	{
		return builds.get(name);
	}

	public Collection<RaidBuild> getBuilds()
	{
		return Collections.unmodifiableCollection(builds.values());
	}

	public RaidMember getMember(String name)
	{
		return members.get(name);
	}

	public Collection<RaidMember> getMembers()
	{
		return Collections.unmodifiableCollection(members.values());
	}
}
