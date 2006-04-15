package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.state.userlist.User;

public class FileResponse {

	private long size;
	private long offset;
	private int port;
	private User user;
	private byte[] hash;
	private String path;
	private String name;
	
	public long getSize() {
		return size;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public int getPort() {
		return port;
	}
	
	public User getUser() {
		return user;
	}
	
	public byte[] getHash() {
		return hash;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}
}
