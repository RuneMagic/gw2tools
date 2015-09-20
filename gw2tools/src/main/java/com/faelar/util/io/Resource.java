package com.faelar.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public interface Resource
{
	/**
	 * 
	 * @return true if the resource is read-only, false otherwise
	 */
	boolean isReadOnly();

	/**
	 * 
	 * @return true if the resource can be read, false otherwise
	 */
	boolean exists();

	/**
	 * 
	 * @return the name of the resource
	 */
	String getName();

	/**
	 * Opens an InputStream to read data.
	 * @return
	 * @throws IOException
	 */
	InputStream openInput() throws IOException;

	/**
	 * Opens an OutputStream to write data.
	 * @return
	 * @throws IOException
	 */
	OutputStream openOutput() throws IOException;


	boolean copyTo(Resource target);
	boolean writeTo(OutputStream out);
	byte[] getContents();
	boolean readFrom(InputStream in);
	boolean copyFrom(Resource source);
	boolean setContents(byte[] bytes);
	byte[] digest(String algorithm) throws NoSuchAlgorithmException;
	byte[] getMD5Sum();
	byte[] getSHA256Sum();
	byte[] getSHA1Sum();
}
