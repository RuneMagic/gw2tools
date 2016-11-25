package com.runemagic.gw2tools.api;

import java.util.Arrays;
import java.util.List;

public class GW2APIRequest
{
	private final GW2APIVersion version;
	private final String resourcePath;
	private final APIKeyHolder keyHolder;
	private final List<GW2APIParameter> parameters;

	private GW2APIRequest(GW2APIVersion version, String resourcePath, APIKeyHolder keyHolder, List<GW2APIParameter> parameters)
	{
		this.version = version;
		this.resourcePath = resourcePath;
		this.keyHolder = keyHolder;
		this.parameters = parameters;
	}

	public static GW2APIRequest create(String resource, APIKeyHolder keyHolder)
	{
		return new GW2APIRequest(GW2APIVersion.V2, resource, keyHolder, null);
	}

	public static GW2APIRequest createV2(String resource)
	{
		return new GW2APIRequest(GW2APIVersion.V2, resource, null, null);
	}

	public static GW2APIRequest createV1(String resource, GW2APIParameter... parameters)
	{
		return new GW2APIRequest(GW2APIVersion.V1, resource, null, Arrays.asList(parameters));
	}

	public static class GW2APIParameter
	{
		public final String name;
		public final String value;

		public GW2APIParameter(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
	}
}
