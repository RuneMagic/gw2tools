package com.runemagic.gw2tools.api;

import com.google.gson.JsonElement;

/**
 * Basic interface for accessing GW2 API resources.<br>
 */
public interface GW2APISource
{
	JsonElement readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException;
	JsonElement readAPIv2Resource(String resource) throws GW2APIException;
	JsonElement readAPIv1Resource(String resource, String... parameters) throws GW2APIException;//TODO parameter object
}
