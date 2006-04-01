package org.lunarray.lshare.protocol.filelist;

import java.util.List;

import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;

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
		if (path.equals(".")) {
			return receiver.getEntries(path);
		} if (path.equals("")) {
			return receiver.getEntries(name);
		} else {
			return receiver.getEntries(path + SharedDirectory.SEPARATOR + name);
		}
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
