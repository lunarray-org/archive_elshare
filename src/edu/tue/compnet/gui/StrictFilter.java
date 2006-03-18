package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;

/**
 * Constructs a strict filter that searches for a specific filename.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class StrictFilter implements SearchFilter {
	// The search string
	String search;

	/**
	 * Constructs a strict filter for a specific filename.
	 * @param s The filename.
	 */
	public StrictFilter(String s) {
		search = s;
	}
	
	/**
	 * Gets the name of the filter, the 'title'.
	 */
	public String getName() {		
		return "Search for filename: " + search;
	}

	/**
	 * Checks wether it's allowed
	 */
	public boolean isAllowed(SearchEvent e) {
		if (e.getName().equals(search)) {
			return true;
		} else {
			return false;
		}
	}
}
