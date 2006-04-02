package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

public class StringFilter implements SearchFilter {

	private String filter;
	
	public StringFilter(String f) {
		filter = f;
	}
	
	public boolean isValid(SearchEvent res) {
		if (res.getEntry().getName().contains(filter)) {
			return true;
		} else {
			return false;
		}		
	}

	public String getName() {
		return "Search: " + filter;
	}
}
