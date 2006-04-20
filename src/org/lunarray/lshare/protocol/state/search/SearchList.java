package org.lunarray.lshare.protocol.state.search;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;
import org.lunarray.lshare.protocol.packets.search.SearchOut;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * The search list for handling searching.
 * @author Pal Hargitai
 */
public class SearchList implements ExternalSearchList {
    /**
     * The registered listeners.
     */
    private ArrayList<SearchListener> listeners;

    /**
     * The controls for the rest of the protocol.
     */
    private Controls controls;

    /**
     * Constructs the search list.
     * @param c The controls to the protocol.
     */
    public SearchList(Controls c) {
        listeners = new ArrayList<SearchListener>();
        controls = c;
    }

    /**
     * Registeres a listener for search results.
     * @param lis The listener to register.
     */
    public void addListener(SearchListener lis) {
        listeners.add(lis);
    }

    /**
     * Unregisteres a listener for search results.
     * @param lis The listener to unregister.
     */
    public void removeListener(SearchListener lis) {
        listeners.remove(lis);
    }

    /**
     * Process a search result.
     * @param e A single search result to handle.
     */
    public void processResult(SearchResult e) {
        Controls.getLogger().fine("Result received: " + e.getName());
        Controls.getLogger().fine("Form: " + e.getAddress().getHostName());

        User u;
        try {
            u = controls.getState().getUserList().findUserByAddress(
                    e.getAddress());
        } catch (UserNotFound unf) {
            u = new User("", e.getAddress(), "<not logged on>", false, null);
        }

        SearchEvent ev = new SearchEvent(e, this, u);
        for (SearchListener l : listeners) {
            l.searchResult(ev);
        }
    }

    /**
     * Search for a specific search string.
     * @param s The search string to search for.
     */
    public void searchForString(String s) {
        SearchOut so = new SearchOut(s);
        controls.getUDPTransport().send(so);
    }

    /**
     * Search for a specific file hash.
     * @param h The hash to search for.
     */
    public void searchForHash(Hash h) {
        // TODO
    }
}
