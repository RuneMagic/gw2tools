package com.faelar.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractResource implements Resource
{
	private boolean readonly=false;

	public AbstractResource()
	{

	}

	public AbstractResource(boolean readonly)
	{
		this.readonly=readonly;
	}


	@Override
	public boolean isReadOnly()
	{
		return readonly;
	}


	@Override
	public boolean copyTo(Resource target)
	{
		try
		{
			return writeTo(target.openOutput());
		}
		catch (IOException e)
		{
			return false;
		}
	}

	@Override
	public boolean writeTo(OutputStream out)
	{
		InputStream in=null;
		try
		{
			in=openInput();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in!=null) in.close();
				if (out!=null) out.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public byte[] getContents()
	{
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		if (writeTo(out)) return out.toByteArray();
		return null;
	}

	public String getContentsString()
	{
		try
		{
			return getContentsString("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getContentsString(String encoding) throws UnsupportedEncodingException
	{
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		if (writeTo(out)) return out.toString(encoding);//TODO use reader/writer instead
		return null;
	}


	@Override
	public boolean readFrom(InputStream in)
	{
		if (isReadOnly()) return false;
		OutputStream out=null;
		try
		{
			out=openOutput();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in!=null) in.close();
				if (out!=null) out.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean copyFrom(Resource source)
	{
		try
		{
			return readFrom(source.openInput());
		}
		catch (IOException e)
		{
			return false;
		}
	}

	@Override
	public boolean setContents(byte[] bytes)
	{
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		return readFrom(in);
	}


	public boolean setContentsString(String str)
	{
		try
		{
			return setContentsString(str, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean setContentsString(String str, String encoding) throws UnsupportedEncodingException
	{
		ByteArrayInputStream in=new ByteArrayInputStream(str.getBytes(encoding));//TODO use reader/writer instead
		if (!readFrom(in)) return false;
		return true;
	}

	@Override
	public byte[] digest(String algorithm) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		return md.digest(getContents());
	}

	@Override
	public byte[] getMD5Sum()
	{
		try
		{
			return digest("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public byte[] getSHA256Sum()
	{
		try
		{
			return digest("SHA-256");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public byte[] getSHA1Sum()
	{
		try
		{
			return digest("SHA-1");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}


	/*public boolean contentsEquals(Resource other)
	{
		InputStream is1 = null;
		InputStream is2 = null;

		try {
			is1 = openInput();
			is2 = other.openInput();

			return inputStreamEquals(is1, is2);

		} catch (Exception ei) {
			return false;
		} finally {
			try {
				if(is1 != null) is1.close();
				if(is2 != null) is2.close();
			} catch (Exception ei2) {ei2.printStackTrace();}
		}
	}*/
}
