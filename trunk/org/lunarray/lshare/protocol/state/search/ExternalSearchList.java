package org.lunarray.lshare.protocol.state.search;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.events.SearchListener;

/**
 * Defines an interface to the search list usable by an external program.
 * @author Pal Hargitai
 */
public interface ExternalSearchList {
    /**
     * Registeres a listener to listen for search events.
     * @param lis The listener to register.
     */
    public void addListener(SearchListener lis);

    /**
     * Unregisters a listener from listening for search events.
     * @param lis The listener to unregister.
     */
    public void removeListener(SearchListener lis);

    /**
     * Search for a specific file string.
     * @param s The string to search for.
     */
    public void searchForString(String s);

    /**
     * Search for a specific file hash.
     * @param h The hash to search for.
     */
    public void searchForHash(Hash h);
}
