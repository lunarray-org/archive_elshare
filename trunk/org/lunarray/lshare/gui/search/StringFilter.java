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
package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

/**
 * A regular filter that searches a result for a matching string.
 * @author Pal Hargitai
 */
public class StringFilter implements SearchFilter {
    /**
     * The string to filter on.
     */
    private String filter;

    /**
     * Constructs a filter with a specified string.
     * @param f The string to filter on.
     */
    public StringFilter(String f) {
        filter = f;
    }

    /**
     * Checks wether a search result is valid to be processed.
     * @param res The result to check.
     * @return True if the name of the entry in the result contains the
     * specified search string.
     */
    public boolean isValid(SearchEvent res) {
        return res.getEntry().getName().contains(filter);
    }

    /**
     * Gets the name of this filter.
     * @return The name of this filter, generally the search string.
     */
    public String getName() {
        return filter;
    }
}
