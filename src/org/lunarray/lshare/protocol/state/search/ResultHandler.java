package org.lunarray.lshare.protocol.state.search;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/** A handler for a search result.
 * @author Pal Hargitai
 */
public class ResultHandler implements RunnableTask {
	/** The result to handle.
	 */
	private SearchResult result;
	
	/** Constructs a result handler.
	 * @param r The result to handle.
	 */
	public ResultHandler(SearchResult r) {
		result = r;
	}
	
	/** Processes the search results.
	 * @param c The controls to the protocol.
	 */
	public void runTask(Controls c) {
		c.getState().getSearchList().processResult(result);
	}
}
