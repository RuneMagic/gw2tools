package com.runemagic.gw2tools.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface ThreadManager
{
	public ExecutorService getExecutor(String key);

	public void shutdown();

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

	public default boolean awaitTermination(long timeout) throws InterruptedException
	{
		return awaitTermination(timeout, TimeUnit.MILLISECONDS);
	}
}
