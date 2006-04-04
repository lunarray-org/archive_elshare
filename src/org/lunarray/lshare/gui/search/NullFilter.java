package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

/**
 * An empty filter that just ignores any search results.
 * @author Pal Hargitai
 */
public class NullFilter implements SearchFilter {

	/**
	 * Checks wether a given result should be processed.
	 * @param r The result to check.
	 * @return False, no result should be processed.
	 */
	public boolean isValid(SearchEvent r) {
		return false;
	}

	/**
	 * Gets the name of this filter.
	 * @return The empty string, as it's not supposed to do anything.
	 */
	public String getName() {
		return "";
	}

}
