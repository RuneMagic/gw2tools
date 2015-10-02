package com.runemagic.gw2tools.api;

public class GW2APIException extends Exception
{

	public GW2APIException()
	{

	}

	public GW2APIException(String message)
	{
		super(message);
	}

	public GW2APIException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GW2APIException(Throwable cause)
	{
		super(cause);
	}

}
