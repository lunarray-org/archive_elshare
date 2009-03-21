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

import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserList;

/**
 * The user event that is sent to user listeners.
 * @author Pal Hargitai
 */
public class UserEvent {
    /**
     * The user associated with this event.
     */
    private User user;

    /**
     * The userlist that triggerd this event.
     */
    private UserList ulist;

    /**
     * Constructs a user event.
     * @param u The user associated with this event.
     * @param l The source that triggered this event.
     */
    public UserEvent(User u, UserList l) {
        user = u;
        ulist = l;
    }

    /**
     * Gets the user associated with this event.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the source of this event.
     * @return The source that triggered this event.
     */
    public UserList getUserList() {
        return ulist;
    }
}
