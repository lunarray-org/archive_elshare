package edu.tue.compnet.protocol.state;

import java.io.File;
import java.net.InetAddress;

/**
 * A file from a user that is allowed to be downloaded.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Allowed {
	// The address
	InetAddress address;
	// The file
	File file;
	
	/**
	 * The constructor of an allowed item.
	 * @param a The address from which the file is allowed.
	 * @param f The file of which a frequest from the address is allowed.
	 */
	public Allowed(InetAddress a, File f) {
		file = f;
		address = a;
	}
	
	/**
	 * Wether it's equal, ie. allowed.
	 * @param a The address to check on.
	 * @param f The file to check on.
	 * @return True of they match, false if not.
	 */
	public boolean equals(InetAddress a, File f) {
		return file.equals(f) && address.equals(a);
	}
}
