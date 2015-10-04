package com.runemagic.gw2tools.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DefaultGW2APISource implements GW2APISource
{
	private final static String API_URL_V2="https://api.guildwars2.com/v2";

	public DefaultGW2APISource()
	{

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
