package edu.tue.compnet.protocol.state;

import java.net.InetAddress;
import java.util.ArrayList;

import edu.tue.compnet.events.SearchEvent;
import edu.tue.compnet.events.SearchListener;
import edu.tue.compnet.protocol.State;

/**
 * Handles events designated for search listeners.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class SearchList {
	// The listeners to notify on events
	ArrayList<SearchListener> searchlisteners;
	// The state of the protocol
	State state;

	/**
	 * The constructor of the search list.
	 * @param s The state of the protocol.
	 */
	public SearchList(State s) {
		searchlisteners = new ArrayList<SearchListener>();
		state = s;
	}
	
	/**
	 * Add a listener to notify when events happen.
	 * @param lis The listener to add.
	 */
	public synchronized void addSearchListener(SearchListener lis) {
		searchlisteners.add(lis);
	}
	
	/**
	 * Remove a listener from the list.
	 * @param lis The listener to remove.
	 */
	public synchronized void removeSearchListener(SearchListener lis) {
		searchlisteners.remove(lis);
	}
	
	/**
	 * A search result came in and will be passed.
	 * @param name The name of the file.
	 * @param size The size of the file.
	 * @param date The modification/create date.
	 * @param a The address where the result came from.
	 * @param hash The hash of the result
	 */
	public synchronized void searchResult(String name, int size, int date,
			InetAddress a, byte[] hash) {
		String nick = "anonymous";
		search: {
			for (ListUser lu: state.getUserList().getUserList()) {
				if (lu.getAddress().equals(a)) {
					nick = lu.getName();
					break search;
				}
			}
		}
		SearchEvent ev = new SearchEvent(state, a, nick, name, size, date,
				hash);
		for (SearchListener lis: searchlisteners) {
			lis.searchResult(ev);
		}
	}
}
