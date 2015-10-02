package com.runemagic.gw2tools.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.runemagic.gw2tools.api.character.GW2Account;
import com.runemagic.gw2tools.api.character.GW2Character;

public class GW2API implements GW2APISource
{
	private final static String API_URL_V2="https://api.guildwars2.com/v2";

	private final static GW2API instance=new GW2API();

	public GW2API()
	{

	}

	public static GW2API inst()
	{
		return instance;
	}

	private String appendAccessToken(String url, APIKeyHolder keyHolder)
	{
		return url+"?access_token="+keyHolder.getAPIKey();
	}

	@Override
	public String readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException
	{
		return readAPIv2Resource(appendAccessToken(resource, keyHolder));
	}

	@Override
	public String readAPIv2Resource(String resource) throws GW2APIException
	{
		try
		{
			//TODO basic validation
			return readUrl(API_URL_V2+"/"+resource);
		}
		catch (IOException e)
		{
			throw new GW2APIException(e);
		}
	}

	public GW2Account getAccount(String apiKey)
	{
		GW2Account acc=new GW2Account(this, apiKey);
		acc.update();
		return acc;
	}

	public GW2Character getCharacter(String name, String apiKey)
	{
		GW2Character character=new GW2Character(this, name, apiKey);
		character.update();
		return character;
	}

	private String readUrl(String urlString) throws IOException
	{
		try (BufferedReader reader= new BufferedReader(new InputStreamReader((new URL(urlString)).openStream()))) {
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		}
	}
}
