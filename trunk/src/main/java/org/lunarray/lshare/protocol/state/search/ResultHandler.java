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

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A handler for a search result.
 * @author Pal Hargitai
 */
public class ResultHandler implements RunnableTask {
    /**
     * The result to handle.
     */
    private SearchResult result;

    /**
     * Constructs a result handler.
     * @param r The result to handle.
     */
    public ResultHandler(SearchResult r) {
        result = r;
    }

    /**
     * Processes the search results.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        c.getState().getSearchList().processResult(result);
    }
}
