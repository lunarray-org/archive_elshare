package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;

/**
 * A regular filter that sees if a file matches.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class RegularFilter implements SearchFilter {
	// The search string
	String search;
	
	/**
	 * The filter constructor.
	 * @param s The search string.
	 */
	public RegularFilter(String s) {
		search = s;
	}
	
	/**
	 * Gets the name of the filter, the 'title'.
	 */
	public String getName() {		
		return "Search: " + search;
	}

	/**
	 * Checks wether it's allowed
	 */
	public boolean isAllowed(SearchEvent e) {
		if (e.getName().contains(search)) {
			return true;
		} else {
			return false;
		}
	}
}