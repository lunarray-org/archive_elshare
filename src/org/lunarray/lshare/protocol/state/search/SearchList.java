package org.lunarray.lshare.protocol.state.search;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;
import org.lunarray.lshare.protocol.packets.search.SearchOut;
import org.lunarray.lshare.protocol.state.userlist.User;

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
	
	public void processResult(SearchResult e) {
		Controls.getLogger().fine("Result received: " + e.getName());
		Controls.getLogger().fine("Form: " + e.getAddress().getHostName());
		
		User u = controls.getState().getUserList().findUserByAddress(e.getAddress());
		if (u == null) {
			u = new User("", e.getAddress(), "<not logged on>", false, null);
		}
		
		SearchEvent ev = new SearchEvent(e, this, u);
		for (SearchListener l: listeners) {
			l.searchResult(ev);
		}
	}
	
	public void searchForString(String s) {
		SearchOut so = new SearchOut(s);
		controls.getUDPTransport().send(so);
	}
	
	public void searchForHash(byte[] h) {
		// TODO
	}
}
