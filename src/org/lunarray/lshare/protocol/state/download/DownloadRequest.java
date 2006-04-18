package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

public class DownloadRequest extends RemoteFile {

	private long offset;
	
	public DownloadRequest(String p, String n, Hash h, long s, long o) {
		super(p, n, h, 1, s);
	}
	
	public long getOffset() {
		return offset;
	}
}
