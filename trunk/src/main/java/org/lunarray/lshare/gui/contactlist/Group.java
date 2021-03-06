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
package org.lunarray.lshare.gui.contactlist;

import java.util.ArrayList;
import java.util.Collections;

import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * Represents a certain kind of users. Either online users, online buddies or
 * offline buddies.
 * @author Pal Hargitai
 */
public class Group {
    /**
     * The name of the group.
     */
    private String name;

    /**
     * The users in this group.
     */
    private ArrayList<UserNode> ulist;

    /**
     * Instanciates the group.
     * @param n The name of the group.
     */
    public Group(String n) {
        name = n;
        ulist = new ArrayList<UserNode>();
    }

    /**
     * Gets the child at a specified index.
     * @param arg0 The index of the child to get.
     * @return The child if it exists, else null.
     */
    public UserNode getChildAt(int arg0) {
        return 0 <= arg0 && arg0 < ulist.size() ? ulist.get(arg0) : null;
    }

    /**
     * Gets the amount of children in the group.
     * @return The amount of children.
     */
    public int getChildCount() {
        return ulist.size();
    }

    /**
     * Gets the index of a specified object.
     * @param arg0 The object to get the index of.
     * @return The index of the specified object.
     */
    public int getIndex(Object arg0) {
        return ulist.contains(arg0) ? ulist.indexOf(arg0) : -1;
    }

    /**
     * The string representation of this group.
     * @return The string representation of this group.
     */
    public String toString() {
        return name;
    }

    /**
     * Resort the list to match new user names.
     * @param n The node to resort for.
     */
    protected void resort(UserNode n) {
        if (ulist.contains(n)) {
            ulist.remove(n);

            int i = Collections.binarySearch(ulist, n.getUser());
            ulist.add(i < 0 ? ~i : i, n);
        }
    }

    /**
     * Remove a specified user from the group.
     * @param u The user to remove.
     * @return The node that had this user associated with it.
     */
    protected UserNode removeUser(User u) {
        UserNode toret = findUser(u);
        if (toret != null) {
            ulist.remove(toret);
        }
        return toret;
    }

    /**
     * Finds a node associated with a specified user.
     * @param u The user to find the associated node of.
     * @return The node that has the specified user associated with it.
     */
    protected UserNode findUser(User u) {
        for (UserNode n : ulist) {
            if (u.equals(n.getUser())) {
                return n;
            }
        }
        return null;
    }

    /**
     * Add a node to the group associates the user with it.
     * @param u The user to add to the group.
     * @return The node that has the specified user associated with it.
     */
    protected UserNode addUser(User u) {
        UserNode n = new UserNode(this, u);
        int i = Collections.binarySearch(ulist, u);

        ulist.add(i < 0 ? ~i : i, n);
        return n;
    }
}
