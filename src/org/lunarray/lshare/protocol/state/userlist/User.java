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
    public User(String h, InetAddress a, String n, boolean b, UserList l) {
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
     * Set the users name.
     * @param nu The new name of the user.
     */
    public void setName(String nu) {
        name = nu;
    }

    /**
     * Get the hostname of the user.
     * @return The hostname.
     */
    public String getHostname() {
        if (address != null) {
            return address.getHostName();
        } else {
            return "";
        }
    }

    /**
     * Get a string representation of the host address of the user.
     * @return The host address.
     */
    public String getHostaddress() {
        if (address != null) {
            return address.getHostAddress();
        } else {
            return "";
        }
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
     * Set this user to be a buddy.
     */
    public void setBuddy() {
        if (ulist != null) {
            ulist.addBuddy(this);
            isbuddy = true;
        }
    }

    /**
     * Unset this user to be a buddy.
     */
    public void unsetBuddy() {
        if (ulist != null) {
            ulist.removeBuddy(this);
            isbuddy = false;
        }
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

    @Override
    /**
     * Checks if this equals another user.
     * @return True if this user is another user, false if not.
     */
    public boolean equals(Object arg0) {
        if (arg0.getClass().equals(User.class)) {
            User u = (User) arg0;
            if (challengeMatches(u.challenge)) {
                return true;
            } else {
                if (address != null && u.address != null) {
                    return address.equals(u.address);
                } else {
                    return false;
                }
            }
        }
        return false;
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
     * Get this users challenge.
     * @return The users challenge.
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * Checks wether the given challenge matches
     * @param h The challenge to compare to.
     * @return True if the challenges match, false if not.
     */
    protected boolean challengeMatches(String h) {
        if (challenge.length() <= 0) {
            return false;
        } else {
            return challenge.equals(h);
        }
    }

    /**
     * Set this users address.
     * @param a The address to set to.
     */
    protected void setAddress(InetAddress a) {
        address = a;
    }
}
