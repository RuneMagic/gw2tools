package com.runemagic.gw2tools.raid;

import java.util.Set;

public interface RaidMemberLoader
{
	Set<RaidMember> getMembers() throws MemberLoaderException;
}
