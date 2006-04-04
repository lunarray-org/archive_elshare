package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

/**
 * Defines an interface of a search filter. Any filter that wants to filter
 * search results should abide this interface. 
 * @author Pal Hargitai
 */
public interface SearchFilter {

	/**
	 * Checks wether a given search result should be processed.
	 * @param r The result to check.
	 * @return True if the result should be processed, false if not.
	 */
	public boolean isValid(SearchEvent r); 
	
	/**
	 * Gets the string representation of this search filter. Usually a string
	 * representation of what this filter is filtering for.
	 * @return The string representation of this filter.
	 */
	public String getName();
}
