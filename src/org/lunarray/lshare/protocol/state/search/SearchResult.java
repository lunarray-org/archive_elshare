package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.RemoteFile;

public class SearchResult extends RemoteFile {

	private InetAddress address;
	
	public SearchResult(InetAddress a, String p, String n, byte[] h, long lm, long s) {
		super(p, n, h, lm, s);
		address = a;
	}
	
	public InetAddress getAddress() {
		return address;
	}
}
