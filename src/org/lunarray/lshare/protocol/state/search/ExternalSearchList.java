package org.lunarray.lshare.protocol.state.search;

import org.lunarray.lshare.protocol.events.SearchListener;

public interface ExternalSearchList {

	public void addListener(SearchListener lis);
	
	public void removeListener(SearchListener lis);
	
	public void searchForString(String s);
	
	public void searchForHash(byte[] h);
	
}
