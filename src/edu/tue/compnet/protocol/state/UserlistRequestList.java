package edu.tue.compnet.protocol.state;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a list of requests for userlists.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class UserlistRequestList {
	// The list of requests
	List<InetAddress> list;
	
	/**
	 * The constructor of the userlistrequest list
	 */
	public UserlistRequestList() {
		list = new ArrayList<InetAddress>();
	}
	
	/**
	 * Takes a userlist request out of the list if it exists.
	 * @param ad The address to take
	 * @return True if address ad has been removed, false if not.
	 */
	public synchronized boolean take(InetAddress ad) {
		InetAddress a = null;
		search: {
			for (InetAddress b: list) {
				if (ad.equals(b)) {
					a = b;
					break search;
				}
			}
		}
		if (a == null) {
			return false;
		} else {
			list.remove(a);
			return true;
		}
	}
	
	/**
	 * Checks if a request exists.
	 * @param ad The request to check.
	 * @return True if it exists, false if not.
	 */
	public synchronized boolean exists(InetAddress ad) {
		boolean ret = false;
		search: {
			for (InetAddress a: list) {
				if (ad.equals(a)) {
					ret = true;
					break search;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Registered a request so that it may be checked for timeouts
	 * @param a The address to add
	 */
	public synchronized void put(InetAddress a) {
		list.add(a);
	}
}
