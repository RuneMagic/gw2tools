package com.runemagic.gw2tools.raid;

import java.util.Set;

public interface RaidDataInterface
{
	Set<RaidMember> getMembers() throws RaidInterfaceException;
	Set<RaidBuild> getBuilds() throws RaidInterfaceException;
	Set<RaidRole> getRoles() throws RaidInterfaceException;
	SessionBase getSessionBase() throws RaidInterfaceException;
}
