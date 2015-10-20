package com.runemagic.gw2tools.util;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GlobalPoolThreadManager implements ThreadManager
{
	private final ExecutorService globalPool;
	private final ScheduledExecutorService globalScheduledPool;

	public GlobalPoolThreadManager()
	{
		this(Executors.newCachedThreadPool(), Executors.newScheduledThreadPool(1));//TODO max thread limit
	}

	public GlobalPoolThreadManager(ExecutorService globalPool)
	{
		this(globalPool, Executors.newScheduledThreadPool(1));
	}

	public GlobalPoolThreadManager(ExecutorService globalPool, ScheduledExecutorService globalScheduledPool)
	{
		Objects.requireNonNull(globalPool);
		Objects.requireNonNull(globalScheduledPool);
		this.globalPool=Executors.unconfigurableExecutorService(globalPool);
		this.globalScheduledPool=Executors.unconfigurableScheduledExecutorService(globalScheduledPool);
	}

	@Override
	public ExecutorService getExecutor(String key)
	{
		return globalPool;
	}

	@Override public ScheduledExecutorService getScheduledExecutor(String key)
	{
		return globalScheduledPool;
	}

	@Override
	public void shutdown()
	{
		globalPool.shutdown();
		globalScheduledPool.shutdown();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{
		long time=timeout/2;
		boolean ret=globalPool.awaitTermination(time, unit);
		ret&=globalScheduledPool.awaitTermination(timeout-time, unit);
		return ret;
	}

}
