package edu.tue.compnet.events;

/**
 * Listens for search results.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface SearchListener {
	/**
	 * Search result.
	 */
	void searchResult(SearchEvent e);
}
