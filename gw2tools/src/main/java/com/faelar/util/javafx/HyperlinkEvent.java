package com.faelar.util.javafx;

import java.net.URL;

public class HyperlinkEvent
{
	private final URL url;

	private boolean canceled=false;

	public HyperlinkEvent(URL url)
	{
		this.url = url;
	}

	public void setCanceled(boolean canceled)
	{
		this.canceled = canceled;
	}

	public URL getUrl()
	{
		return url;
	}

	public boolean isCanceled()
	{
		return canceled;
	}

}
