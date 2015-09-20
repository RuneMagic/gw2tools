package com.runemagic.gw2tools.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GlobalPoolThreadManager implements ThreadManager
{
	private ExecutorService globalPool;

	public GlobalPoolThreadManager()
	{
		globalPool=Executors.newCachedThreadPool();//TODO max thread limit
	}

	@Override
	public ExecutorService getExecutor(String key)
	{
		return globalPool;
	}

	@Override
	public void shutdown()
	{
		globalPool.shutdown();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{
		return globalPool.awaitTermination(timeout, unit);
	}

}
