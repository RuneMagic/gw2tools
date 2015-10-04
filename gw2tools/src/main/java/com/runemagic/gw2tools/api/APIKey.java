package com.runemagic.gw2tools.api;

import java.util.Objects;

public final class APIKey implements Comparable<APIKey>
{
	private final String key;

	public APIKey(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	public String toString()
	{
		return getKey();
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		APIKey apiKey = (APIKey) o;
		return Objects.equals(key, apiKey.key);
	}

	@Override public int hashCode()
	{
		return Objects.hash(key);
	}

	@Override public int compareTo(APIKey o)
	{
		return 0;
	}
}
