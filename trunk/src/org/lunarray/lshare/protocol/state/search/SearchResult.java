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

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A search result.
 * @author Pal Hargitai
 */
public class SearchResult extends RemoteFile {
    /**
     * The address the search result came from.
     */
    private InetAddress address;

    /**
     * Constructs a search result.
     * @param a The address from where the result originated.
     * @param p The path of the result.
     * @param n The name of the result.
     * @param h The hash of the result.
     * @param lm The last modified date of the result.
     * @param s The size of the result.
     */
    public SearchResult(InetAddress a, String p, String n, Hash h, long lm,
            long s) {
        super(p, n, h, lm, s);
        address = a;
    }

    /**
     * Gets the address from where the result came.
     * @return The address from where the result came.
     */
    public InetAddress getAddress() {
        return address;
    }
}
