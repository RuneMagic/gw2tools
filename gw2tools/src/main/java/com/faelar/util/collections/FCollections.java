package com.faelar.util.collections;

import java.util.List;

public final class FCollections
{
	private FCollections() {}

	public static <T,E> List<E> convertList(List<T> list, ContentAdapter<T,E> adapter)
	{
		return new ListAdapter<T,E>(list, adapter);
	}
}
