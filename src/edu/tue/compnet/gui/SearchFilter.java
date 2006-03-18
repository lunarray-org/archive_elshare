package edu.tue.compnet.gui;

import edu.tue.compnet.events.SearchEvent;

/**
 * A filter class to allow further customisation of the search pane.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface SearchFilter {
	/**
	 * Get the name of the filter, ie. for search title
	 * @return The name.
	 */
	String getName();
	
	/**
	 * Asks wether the event is allowed to be processed.
	 * @param e The event.
	 * @return True if the event is allowed, false if not
	 */
	boolean isAllowed(SearchEvent e);
}
