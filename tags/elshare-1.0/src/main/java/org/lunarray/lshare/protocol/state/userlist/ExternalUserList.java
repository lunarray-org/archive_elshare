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
package org.lunarray.lshare.protocol.state.userlist;

import java.util.List;

import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;

/**
 * The external itnerface for user list management.
 * @author Pal Hargitai
 */
public interface ExternalUserList {
    /**
     * Get the current userlist.
     * @return The userlist.
     */
    List<User> getUserList();

    /**
     * Adds a listener for user events.
     * @param lis The listener to add.
     */
    void addListener(UserListener lis);

    /**
     * Removes a listener for user events.
     * @param lis The listener to remove.
     */
    void removeListener(UserListener lis);

    /**
     * Gets the filelist for the specified user.
     * @param u The user to get the filelist of.
     * @return The filelist of the specified user.
     */
    FilelistEntry getFilelist(User u);
}
