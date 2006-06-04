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
 * Defines an interface of a search filter. Any filter that wants to filter
 * search results should abide this interface.
 * @author Pal Hargitai
 */
public interface SearchFilter {

    /**
     * Checks wether a given search result should be processed.
     * @param r The result to check.
     * @return True if the result should be processed, false if not.
     */
    public boolean isValid(SearchEvent r);

    /**
     * Gets the string representation of this search filter. Usually a string
     * representation of what this filter is filtering for.
     * @return The string representation of this filter.
     */
    public String getName();
}
