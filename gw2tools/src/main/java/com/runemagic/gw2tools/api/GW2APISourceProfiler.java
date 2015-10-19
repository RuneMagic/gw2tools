package com.runemagic.gw2tools.api;
import com.google.gson.JsonElement;

public class GW2APISourceProfiler implements GW2APISource
{
	private long apiV2Calls=0;
	private long apiV2AuthCalls=0;
	private long apiV1Calls=0;
	private long totalCalls=0;

	private final GW2APISource src;

	public GW2APISourceProfiler(GW2APISource src)
	{
		this.src=src;
	}

	@Override
	public JsonElement readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException
	{
		apiV2AuthCalls++;
		return src.readAPIv2Resource(resource, keyHolder);
	}

	@Override
	public JsonElement readAPIv2Resource(String resource) throws GW2APIException
	{
		apiV2Calls++;
		return src.readAPIv2Resource(resource);
	}

	@Override
	public JsonElement readAPIv1Resource(String resource, String... parameters) throws GW2APIException
	{
		apiV1Calls++;
		return src.readAPIv1Resource(resource, parameters);
	}

	public long getTotalCalls()
	{
		return apiV2Calls+apiV2AuthCalls+apiV1Calls;
	}

	public long getAPIv2Calls()
	{
		return apiV2Calls;
	}

	public long getAPIv2AuthCalls()
	{
		return apiV2AuthCalls;
	}

	public long getAPIv1Calls()
	{
		return apiV1Calls;
	}

}
