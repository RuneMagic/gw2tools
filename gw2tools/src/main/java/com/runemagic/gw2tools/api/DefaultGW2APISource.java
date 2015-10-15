package com.runemagic.gw2tools.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DefaultGW2APISource implements GW2APISource
{
	private final static String API_URL_V1="https://api.guildwars2.com/v1";
	private final static String API_URL_V2="https://api.guildwars2.com/v2";

	private final JsonParser gson=new JsonParser();

	public DefaultGW2APISource()
	{

	}

	private String appendAccessToken(String url, APIKeyHolder keyHolder)
	{
		return url+"?access_token="+keyHolder.getAPIKey();
	}

	@Override
	public JsonElement readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException
	{
		return readAPIv2Resource(appendAccessToken(resource, keyHolder));
	}

	@Override
	public JsonElement readAPIv2Resource(String resource) throws GW2APIException
	{
		try
		{
			String json=readUrl(API_URL_V2+"/"+resource);
			return gson.parse(json);
		}
		catch (IOException e)
		{
			throw new GW2APIException(e);
		}
	}

	@Override
	public JsonElement readAPIv1Resource(String resource, String... parameters) throws GW2APIException
	{
		try
		{
			//TODO basic validation and escape
			StringBuilder sb=new StringBuilder();
			sb.append(API_URL_V1);
			sb.append("/");
			sb.append(resource);
			sb.append(".json");
			int len=parameters.length;
			if (len%2!=0) throw new GW2APIException("Invalid number of API v1 parameters");
			if (len>0) sb.append("?");
			for (int i=0; i<len; i+=2)
			{
				if (i>0) sb.append("&");
				sb.append(parameters[i]);
				sb.append("=");
				sb.append(parameters[i+1]);
			}
			String json=readUrl(sb.toString());
			return gson.parse(json);
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
