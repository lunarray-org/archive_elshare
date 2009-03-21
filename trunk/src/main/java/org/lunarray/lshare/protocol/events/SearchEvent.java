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
package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.search.SearchList;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A search event that should be sent to searchlisteners.
 * @author Pal Hargitai
 */
public class SearchEvent {
    /**
     * The file entry that comes with this event.
     */
    private RemoteFile entry;

    /**
     * The search list that triggered the events.
     */
    private SearchList source;

    /**
     * The user that this event comes from.
     */
    private User user;

    /**
     * Constructs the searchevent.
     * @param e The file that comes with this event.
     * @param s The source of this event, the searchlist.
     * @param u The user that comes with this event.
     */
    public SearchEvent(RemoteFile e, SearchList s, User u) {
        entry = e;
        source = s;
        user = u;
    }

    /**
     * Gets the searchlist that triggered this event.
     * @return The list.
     */
    public SearchList getSource() {
        return source;
    }

    /**
     * Gets the user that this entry belongs to.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * The remote file entry that comes with this event.
     * @return The remote entry.
     */
    public RemoteFile getEntry() {
        return entry;
    }
}
