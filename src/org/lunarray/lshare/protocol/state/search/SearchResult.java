package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A search result.
 * @author Pal Hargitai
 */
public class SearchResult extends RemoteFile {

	/**
	 * The address the search result came from.
	 */
	private InetAddress address;
	
	/**
	 * Constructs a search result.
	 * @param a The address from where the result originated.
	 * @param p The path of the result.
	 * @param n The name of the result.
	 * @param h The hash of the result.
	 * @param lm The last modified date of the result.
	 * @param s The size of the result.
	 */
	public SearchResult(InetAddress a, String p, String n, byte[] h, long lm, long s) {
		super(p, n, h, lm, s);
		address = a;
	}
	
	/**
	 * Gets the address from where the result came.
	 * @return The address from where the result came.
	 */
	public InetAddress getAddress() {
		return address;
	}
}
