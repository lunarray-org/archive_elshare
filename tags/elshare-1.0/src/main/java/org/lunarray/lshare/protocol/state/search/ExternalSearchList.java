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
