package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

public class FileResponse extends RemoteFile {

	private int port;
	private long offset;
	
	public FileResponse(String p, String n, Hash h, long s, long o, int r) {
		super(p, n, h, 1, s);
		port = r;
		offset = o;
	}
	
	public int getPort() {
		return port;
	}
	
	public long getOffset() {
		return offset;
	}
}
