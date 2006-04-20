package org.lunarray.lshare.protocol.events;

/** A search listener that search events are sent to.
 * @author Pal Hargitai
 */
public interface SearchListener {
	/** Triggered if a search result comes in.
	 * @param e The search event that triggered this call.
	 */
	void searchResult(SearchEvent e);
}
