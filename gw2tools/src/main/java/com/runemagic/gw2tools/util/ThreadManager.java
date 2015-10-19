package com.runemagic.gw2tools.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface ThreadManager
{
	ExecutorService getExecutor(String key);

	ScheduledExecutorService getScheduledExecutor(String key);

	void shutdown();

	boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

	default boolean awaitTermination(long timeout) throws InterruptedException
	{
		return awaitTermination(timeout, TimeUnit.MILLISECONDS);
	}
}
