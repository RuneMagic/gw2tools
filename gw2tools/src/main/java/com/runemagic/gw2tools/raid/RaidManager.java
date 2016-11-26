package com.runemagic.gw2tools.raid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaidManager
{
	private static final Logger log = LoggerFactory.getLogger(RaidManager.class);

	private final Set<RaidMember> members=new HashSet<>();
	private final RaidMemberLoader loader;

	public RaidManager(RaidMemberLoader loader)
	{
		this.loader=loader;
	}

	public void initialize()
	{
		loadMembers();
	}


	private void loadMembers()
	{
		members.clear();
		try
		{
			log.debug("Loading members");
			members.addAll(loader.getMembers());
			log.debug("Loaded {} members", members.size());
		}
		catch (MemberLoaderException e)
		{
			e.printStackTrace();
		}
	}

	public Set<RaidMember> getAllMembers()
	{
		return Collections.unmodifiableSet(members);
	}
}
