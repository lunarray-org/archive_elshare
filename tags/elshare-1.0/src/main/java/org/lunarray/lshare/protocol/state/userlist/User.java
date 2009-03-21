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

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

/**
 * A representation of a user.
 * @author Pal Hargitai
 */
public class User implements Comparable<User> {
    /**
     * This users challenge.
     */
    private String challenge;

    /**
     * The address of this user if online, else null.
     */
    private InetAddress address;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * True if this user is a buddy, false if not.
     */
    private boolean isbuddy;

    /**
     * The userlist.
     */
    private UserList ulist;

    /**
     * The time that this user has been online without getting bumped.
     */
    private long time;

    /**
     * Constructs a representation of a user.
     * @param h The users challenge.
     * @param a The users address, or null.
     * @param n The users name.
     * @param b True if the user is a buddy, false if not.
     * @param l The user list.
     */
    protected User(String h, InetAddress a, String n, boolean b, UserList l) {
        challenge = h;
        address = a;
        name = n;
        isbuddy = b;
        ulist = l;
        time = 0;
    }

    /**
     * Get the users address.
     * @return The address.
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Gets the users name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the hostname of the user.
     * @return The hostname.
     */
    public String getHostname() {
        return address != null ? address.getHostName() : "";
    }

    /**
     * Get a string representation of the host address of the user.
     * @return The host address.
     */
    public String getHostaddress() {
        return address != null ? address.getHostAddress() : "";
    }

    /**
     * Asks wether a given user is a buddy.
     * @return True if the user is a buddy, false if not.
     */
    public boolean isBuddy() {
        return isbuddy;
    }

    /**
     * Asks wether the current user is online.
     * @return True if the user is online, false if the user is offline.
     */
    public boolean isOnline() {
        return address != null;
    }

    /**
     * Change this users buddy state.
     * @param isbud Set to true if the user should become a buddy, else false.
     */
    public void setBuddy(boolean isbud) {
        if (ulist != null) {
            if (isbud) {
                ulist.addBuddy(this);
            } else {
                ulist.removeBuddy(this);
            }

        }
        isbuddy = isbud;
    }

    /**
     * Makes this user a buddy.
     */
    @Deprecated
    public void setBuddy() {
        setBuddy(true);
    }

    /**
     * Unmakes this user a buddy.
     */
    @Deprecated
    public void unsetBuddy() {
        setBuddy(false);
    }

    /**
     * Compare this user to another user. Returns < 0 If the user is less than
     * this user. Returns > 0 If the user is greater than this user. Returns = 0
     * If the user equals this user.
     * @param arg0 The user to compare this user to.
     * @return Returns as specified above.
     */
    public int compareTo(User arg0) {
        if (!getHostaddress().equals(arg0.getHostaddress())) {
            if (getName().equals(arg0.getName())) {
                return getHostaddress().compareTo(arg0.getHostaddress());
            } else {
                return getName().compareTo(arg0.getName());
            }
        } else if (!getChallenge().equals(arg0.getChallenge())) {
            return getChallenge().compareTo(arg0.getChallenge());
        } else {
            return 0;
        }
    }

    /**
     * Get this users challenge.
     * @return The users challenge.
     */
    public String getChallenge() {
        return challenge;
    }

    @Override
    /**
     * Checks if this equals another user.
     * @param arg0 The object to compare to.
     * @return True if this user is another user, false if not.
     */
    public boolean equals(Object arg0) {
        if (arg0 instanceof User) {
            User u = (User) arg0;
            if (challengeMatches(u.challenge)) {
                return true;
            } else {
                return address != null && u.address != null ? address
                        .equals(u.address) : false;
            }
        }
        return false;
    }

    /**
     * Gives a string representation of the user.
     * @return The string representation of the user.
     */
    public String toString() {
        return getName()
                + (isOnline() ? "(" + getHostname() + ")" : "(<offline>)");
    }

    /**
     * Set the users name.
     * @param nu The new name of the user.
     */
    protected void setName(String nu) {
        name = nu;
    }

    /**
     * Adds to this users timeout.
     * @param diff The amount of time to add to the users timeout.
     * @return The new time of the users timeout.
     */
    protected long addDiff(long diff) {
        time += diff;
        return time;
    }

    /**
     * Bump this users timeout to 0;
     */
    protected void bump() {
        Controls.getLogger().finer(
                getName() + " bumped time " + "from "
                        + Long.valueOf(time).toString());
        time = 0;
    }

    /**
     * Checks wether the given challenge matches
     * @param h The challenge to compare to. (We assume a challenge isn't
     * empty.)
     * @return True if the challenges match, false if not.
     */
    protected boolean challengeMatches(String h) {
        return challenge.equals(h);
    }

    /**
     * Set this users address.
     * @param a The address to set to.
     */
    protected void setAddress(InetAddress a) {
        address = a;
    }
}
