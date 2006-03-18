package edu.tue.compnet.protocol.state;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * A list of allowed filetransfers. Associated by address and file.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class AllowList {
	// The allowed items
	ArrayList<Allowed> allowed;
	
	/**
	 * The constructor for the allowed list.
	 */
	public AllowList() {
		allowed = new ArrayList<Allowed>();
	}
	
	/**
	 * Wether a given address and file are allowed to be uploaded.
	 * @param ad The address to check on.
	 * @param f The file to check on.
	 * @return True if the address is allowed to download the file, false if
	 * not.
	 */
	public synchronized boolean isAllowed(InetAddress ad, File f) {
		boolean ret = false;
		 search: {
			for (Allowed a: allowed) {
				if (a.equals(ad, f)) {
					ret = true;
					break search;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Allow the given address and file.
	 * @param ad The address to allow.
	 * @param f The file to allow.
	 */
	public synchronized void allow(InetAddress ad, File f) {
		if (!isAllowed(ad, f)) {
			allowed.add(new Allowed(ad, f));
		}
	}
}
