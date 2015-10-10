package com.runemagic.gw2tools.api;

import java.util.Objects;

import com.runemagic.gw2tools.GW2Tools;

public final class APIKey implements Comparable<APIKey>
{
	private final String key;

	public APIKey(String key)
	{
		this.key = key;
	}

	public static APIKey of(String apiKey) throws GW2APIException
	{
		if (!isValid(apiKey)) throw new GW2APIException("Invalid API key");
		return new APIKey(apiKey);
	}

	public static boolean isValid(String key)
	{
		if (key.length()>72) return false;//this is the only thing ANet specified so we keep it separate
		if (GW2Tools.inst().getAppSettings().noUnofficialValidation.get() &&
				!key.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{20}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) return false;
		return true;
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
