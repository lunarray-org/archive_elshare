package org.lunarray.lshare.protocol.filelist;

import java.util.List;

public class FilelistEntry {

	private String path;
	private String name;
	private byte[] hash;
	private long lastmodified;
	private long size;
	private FilelistReceiver receiver;
	
	public FilelistEntry(FilelistReceiver fr, String p, String n, byte[] h, long lm, long s) {
		path = p;
		name = n;
		hash = h;
		lastmodified = lm;
		size = s;
		receiver = fr;
	}
	
	public void closeReceiver() {
		receiver.close();
	}
	
	public List<FilelistEntry> getEntries() {
		return receiver.getEntries(path);
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
