package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;

/**
 * Implements a filter that allows results from a certain user.
 * Implemented to allow for a userlist.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class UserFilter implements SearchFilter {
	// The username of the user to search for
	String user;
	// The user address
	String address;
	
	/**
	 * The constructor of the user filter.
	 * @param u The username.
	 * @param a The address.
	 */
	public UserFilter(String u, String a) {
		user = u;
		address = a;
	}
	
	/**
	 * Get the name of the search.
	 */
	public String getName() {
		return "Userlist: " + user;
	}
	
	/**
	 * If the hostname is the address, then true else false.
	 */
	public boolean isAllowed(SearchEvent e) {
		return e.getAddress().getHostName().equals(address);
	}
}
