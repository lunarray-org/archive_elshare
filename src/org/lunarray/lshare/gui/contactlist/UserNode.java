package org.lunarray.lshare.gui.contactlist;

import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * The usernode with a specific user associated with it.
 * @author Pal Hargitai
 */
public class UserNode implements Comparable<User> {
    /**
     * The group that the user is contained in.
     */
    private Group group;

    /**
     * The user associated with the node.
     */
    private User user;

    /**
     * Instantiates a user node.
     * @param g The group the node is contained in.
     * @param u The user to associate with the node.
     */
    public UserNode(Group g, User u) {
        group = g;
        user = u;
    }

    /**
     * Gets the user associated with this node.
     * @return The user of this node.
     */
    public User getUser() {
        return user;
    }

    /**
     * Get this nodes parent.
     * @return The parent of this node.
     */
    public Group getParent() {
        return group;
    }

    /**
     * Gets a string representation of this node.
     */
    public String toString() {
        return user.getName() + " (" + user.getHostname() + ")";
    }

    /**
     * Compares a specified user to this nodes user. Returns < 0 if the user is
     * smaller than this nodes user. Returns > 0 if the user is larger than this
     * nodes user. Returns = 0 if the user is equal to this nodes user.
     * @param arg0 The user to compare to.
     * @return The compared value of this node.
     */
    public int compareTo(User arg0) {
        return user.compareTo(arg0);
    }
}
