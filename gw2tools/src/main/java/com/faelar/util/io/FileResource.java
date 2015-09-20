package com.faelar.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileResource extends AbstractResource
{
	private final File file;

	public FileResource(File file)
	{
		if (file==null) throw new IllegalArgumentException("File cannot be null");
		if (file.isDirectory()) throw new IllegalArgumentException("File cannot be a directory");
		this.file=file;
	}


	public FileResource(File file, boolean readonly)
	{
		super(readonly);
		if (file==null) throw new IllegalArgumentException("File cannot be null");
		if (file.isDirectory()) throw new IllegalArgumentException("File cannot be a directory");//TODO do these checks on every access
		this.file=file;
	}

	public static FileResource get(File file)
	{
		return new FileResource(file);
	}

	public File getFile()
	{
		return file;
	}

	/*public void setFile(File file)
	{
		this.file = file;
	}*/

	@Override
	public InputStream openInput() throws FileNotFoundException
	{
		return new FileInputStream(getFile());
	}

	@Override
	public OutputStream openOutput() throws FileNotFoundException
	{
		if (isReadOnly()) throw new FileNotFoundException("This FileResource is in read-only mode");
		File parent=getFile().getParentFile();
		if (parent!=null && !parent.exists() && !parent.mkdirs()) throw new FileNotFoundException("Failed to create parent directory: "+parent);
		return new FileOutputStream(getFile());
	}

	@Override
	public String getName()
	{
		return file.getName();
	}


	public boolean delete()
	{
		return file.delete();
	}

	public URL toURL(File file)
	{
		try {
			return file.toURI().toURL();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copies the file to the target. If the specified path is a directory, then it creates a file with the same name in the directory.
	 * @param target
	 * @return
	 */
	public boolean copyTo(File target)
	{
		if (target==null) return false;
		if (target.isDirectory()) target=new File(target,file.getName());
		return copyTo(new FileResource(target));
	}

	public boolean copyFrom(File source)
	{
		if (source==null || source.isDirectory()) return false;
		return copyFrom(new FileResource(source));
	}

	@Override
	public boolean isReadOnly()
	{
		if (file.exists()&&!file.canWrite()) return true;
		return super.isReadOnly();
	}


	@Override
	public boolean exists()
	{
		return file.exists();
	}
}
