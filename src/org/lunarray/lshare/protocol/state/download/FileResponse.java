package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

public class FileResponse extends RemoteFile {

	private int port;
	private User user;
	
	public FileResponse(String p, String n, Hash h, long s, int o, User u) {
		super(p, n, h, 1, s);
		port = o;
		user = u;
	}
	
	public int getPort() {
		return port;
	}
	
	public User getUser() {
		return user;
	}
}
