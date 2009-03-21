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

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.search.ResultOut;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A search string handler.
 * @author Pal Hargitai
 */
public class StringSearchHandler implements RunnableTask {
    /**
     * Where the search request came from.
     */
    private InetAddress to;

    /**
     * The search string requested.
     */
    private String query;

    /**
     * Constructs a string search handler.
     * @param t The address from which the search originated.
     * @param q The string that was searched for.
     */
    public StringSearchHandler(InetAddress t, String q) {
        to = t;
        query = q;
    }

    /**
     * Process the search request.
     * @param c The controls for the protocol.
     */
    public void runTask(Controls c) {
        for (ShareEntry e : c.getState().getShareList().getEntriesMatching(
                query)) {
            ResultOut o = new ResultOut(to, e);
            c.getUDPTransport().send(o);
        }
    }
}
