package com.faelar.util.io;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceManager
{
	private String resClassPath="";
	private String resFilePath="";

	public ResourceManager(String resClassPath, String resFilePath)
	{
		this.resClassPath=resClassPath;
		this.resFilePath=resFilePath;
	}

	public Resource getResource(String name)
	{
		try {
			File file=new File(resFilePath+File.separator+name);
			if (file.exists()) return new FileResource(file);
			//InputStream ret=ResourceManager.class.getClassLoader().getResourceAsStream(resClassPath+name);
			return new ClassPathResource(ResourceManager.class.getClassLoader(), resClassPath+name);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @deprecated Use getResource() instead
	 * @param name
	 * @return
	 */
	@Deprecated
	public InputStream getResourceAsStream(String name)
	{
		Resource res=getResource(name);
		if (res!=null) try
		{
			return res.openInput();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public URL getResourceURL(String name)
	{
		try {
			File file=new File(resFilePath+File.separator+name);
			if (file.exists()) return file.toURI().toURL();
			URL ret=ResourceManager.class.getClassLoader().getResource(resClassPath+name);
			return ret;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Image getImageResource(String name)
	{
		try
		{
			URL imgURL=null;
			File file=new File(resFilePath+File.separator+name);
			if (file.exists()) imgURL=file.toURI().toURL();
			else imgURL = ResourceManager.class.getClassLoader().getResource(resClassPath+name);

			if (imgURL!=null)
			{
				return Toolkit.getDefaultToolkit().getImage(imgURL);
				//	if (image!=null) this.setIconImage(image);
			}
		}
		catch (Exception e){}
		return null;
	}
}
