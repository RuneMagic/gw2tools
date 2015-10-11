package com.runemagic.gw2tools.api;

/**
 * Basic interface for accessing GW2 API resources.<br>
 */
public interface GW2APISource
{
	String readAPIv2Resource(String resource, APIKeyHolder keyHolder) throws GW2APIException;
	String readAPIv2Resource(String resource) throws GW2APIException;
	String readAPIv1Resource(String resource, String... parameters) throws GW2APIException;//TODO parameter object
}
