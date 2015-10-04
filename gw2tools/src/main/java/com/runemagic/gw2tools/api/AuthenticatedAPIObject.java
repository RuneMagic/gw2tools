package com.runemagic.gw2tools.api;

public abstract class AuthenticatedAPIObject extends AbstractAPIObject implements APIKeyHolder
{
	private final APIKey apiKey;

	public AuthenticatedAPIObject(GW2APISource source, APIKey apiKey)
	{
		super(source);
		this.apiKey=apiKey;
	}

	@Override public APIKey getAPIKey()
	{
		return apiKey;
	}

}
