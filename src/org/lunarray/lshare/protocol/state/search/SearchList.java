package org.lunarray.lshare.protocol.state.search;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.SearchListener;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;
import org.lunarray.lshare.protocol.packets.search.SearchOut;

public class SearchList implements ExternalSearchList {

	private ArrayList<SearchListener> listeners;
	private Controls controls;
	
	public SearchList(Controls c) {
		listeners = new ArrayList<SearchListener>();
		controls = c;
	}

	public void addListener(SearchListener lis) {
		listeners.add(lis);
	}
	
	public void removeListener(SearchListener lis) {
		listeners.remove(lis);
	}
	
	public void processResult(FilelistEntry e) {
		
	}
	
	public void searchForString(String s) {
		SearchOut so = new SearchOut(s);
		controls.getUDPTransport().send(so);
	}
	
	public void searchForHash(byte[] h) {
		
	}
}
