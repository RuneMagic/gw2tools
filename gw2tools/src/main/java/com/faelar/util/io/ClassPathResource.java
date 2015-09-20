package com.faelar.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClassPathResource extends AbstractResource
{
	private ClassLoader classLoader;
	private String path;

	public ClassPathResource(ClassLoader classLoader, String path)
	{
		this.classLoader=classLoader;
		this.path=path;
	}

	public ClassPathResource(String path)
	{
		this.path=path;
	}

	public void setClassLoader(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}


	public void setPath(String path)
	{
		this.path = path;
	}


	public String getPath()
	{
		return path;
	}


	public ClassLoader getClassLoader()
	{
		return classLoader;
	}

	@Override
	public String getName()
	{
		return path;
	}

	@Override
	public InputStream openInput() throws IOException
	{
		if (classLoader==null) return ClassLoader.getSystemResourceAsStream(getPath());
		return classLoader.getResourceAsStream(getPath());
	}


	/**
	 * unsupported
	 */
	@Override
	public OutputStream openOutput() throws IOException
	{
		return null;
	}

	@Override
	public boolean isReadOnly()
	{
		return true;
	}

	@Override
	public boolean exists()
	{
		if (classLoader==null) return ClassLoader.getSystemResource(getPath())!=null;
		return classLoader.getResource(getPath())!=null;
	}

}
