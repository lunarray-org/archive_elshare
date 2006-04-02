package org.lunarray.lshare.protocol;

public abstract class RemoteFile {

	private String path;
	private String name;
	private byte[] hash;
	private long lastmodified;
	private long size;
	
	public RemoteFile(String p, String n, byte[] h, long lm, long s) {
		path = p;
		name = n;
		hash = h;
		lastmodified = lm;
		size = s;
	}

	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}
	
	public byte[] getHash() {
		return hash;
	}
	
	public long getLastModified() {
		return lastmodified;
	}
	
	public long getSize() {
		return size;
	}
	
	public boolean isDirectory() {
		return size < 0;
	}
	
	public boolean isFile() {
		return size >= 0;
	}
}
