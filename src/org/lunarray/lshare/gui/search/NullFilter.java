package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

public class NullFilter implements SearchFilter {

	public boolean isValid(SearchEvent r) {
		return false;
	}

	public String getName() {
		return "";
	}

}
