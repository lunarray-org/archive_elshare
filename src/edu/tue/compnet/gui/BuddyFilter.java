package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;
import edu.tue.compnet.protocol.State;

/**
 * Adds a filter to filter for buddies only.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class BuddyFilter implements SearchFilter {
	// The search string
	String search;
	// The state of the protocol
	State state;
	
	/**
	 * The filter constructor.
	 * @param s The search string.
	 * @param st The state of the protocol.
	 */
	public BuddyFilter(String s, State st) {
		search = s;
		state = st;
	}

	/**
	 * Gets the name of the filter, the 'title'.
	 */
	public String getName() {		
		return "Search(f): " + search;
	}

	/**
	 * Checks wether it's allowed
	 */
	public boolean isAllowed(SearchEvent e) {
		if (state.getBuddyList().isBuddy(e.getAddress().getHostName())) {
			if (e.getName().contains(search)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
