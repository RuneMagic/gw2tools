package com.runemagic.gw2tools.api;

import com.runemagic.gw2tools.api.account.GW2Account;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.api.account.TokenInfo;
import com.runemagic.gw2tools.api.account.World;
import com.runemagic.gw2tools.api.character.GW2Character;

public enum GW2APIClass
{
	ACCOUNT(GW2Account.class),
	CHARACTER(GW2Character.class),
	TOKENINFO(TokenInfo.class),
	GUILD(Guild.class),
	WORLD(World.class);

	private final Class<? extends GW2APIObject> clazz;

	GW2APIClass(Class<? extends GW2APIObject> clazz)
	{
		this.clazz=clazz;
	}
}
