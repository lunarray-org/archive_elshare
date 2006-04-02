package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

public interface SearchFilter {

	public boolean isValid(SearchEvent r); 
	
	public String getName();
}
