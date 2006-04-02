package org.lunarray.lshare.protocol.state.search;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.tasks.RunnableTask;

public class ResultHandler implements RunnableTask {

	private SearchResult result;
	
	public ResultHandler(SearchResult r) {
		result = r;
	}
	
	public void runTask(Controls c) {
		c.getState().getSearchList().processResult(result);
	}

}
