package com.runemagic.gw2tools.api;

import com.runemagic.gw2tools.api.account.TokenInfo;

public abstract class AuthenticatedAPIObject extends AbstractAPIObject implements APIKeyHolder
{
	private final APIKey apiKey;
	private TokenInfo tokenInfo=null;

	public AuthenticatedAPIObject(GW2APISource source, APIKey apiKey)
	{
		super(source);
		this.apiKey=apiKey;
	}

	@Override public APIKey getAPIKey()
	{
		return apiKey;
	}

	public TokenInfo getAPIKeyInfo()
	{
		if (tokenInfo==null) tokenInfo=GW2API.inst().getTokenInfo(getAPIKey());
		return tokenInfo;
	}

}
