package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;
import edu.tue.compnet.protocol.state.HashList;

/**
 * A filter that will search for a specific hash.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class HashFilter implements SearchFilter {
	// The search string
	byte[] hash;
	
	/**
	 * The filter constructor.
	 * @param h The hash.
	 */
	public HashFilter(byte[] h) {
		hash = h;
	}
	
	/**
	 * Gets the name of the filter, the 'title'.
	 */
	public String getName() {		
		return "Search: " + HashList.toString(hash);
	}

	/**
	 * Checks wether it's allowed
	 */
	public boolean isAllowed(SearchEvent e) {
		if (HashList.equals(e.getHash(), hash)) {
			return true;
		} else {
			return false;
		}
	}	
}
