/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.protocol.state.search;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;
import org.lunarray.lshare.protocol.packets.search.SearchOut;
import org.lunarray.lshare.protocol.state.userlist.User;

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

        User u = controls.getState().getUserList().findUserByAddress(
                e.getAddress());
        if (u == null) {
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
        // TODO Search for hash
    }
}
