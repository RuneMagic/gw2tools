package com.runemagic.gw2tools.api;

import java.time.Instant;
import java.util.List;

public enum GW2APIFieldType
{
	DEFAULT(null),

	STRING(String.class),
	NUMBER(Integer.class),
	BOOLEAN(Boolean.class),
	DATETIME(Instant.class),
	ARRAY(List.class),
	OBJECT(Object.class);

	private final Class<?> javaClass;

	GW2APIFieldType(Class<?> javaClass)
	{
		this.javaClass=javaClass;
	}

	public Class<?> getJavaClass()
	{
		return javaClass;
	}
}
