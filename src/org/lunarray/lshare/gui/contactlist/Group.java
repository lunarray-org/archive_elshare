package org.lunarray.lshare.gui.contactlist;

import java.util.ArrayList;

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
		if (0 <= arg0 && arg0 < ulist.size()) {
			return ulist.get(arg0);
		} else {
			return null;
		}
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
		if (ulist.contains(arg0)) {
			return ulist.indexOf(arg0);
		} else {
			return -1;
		}
	}

	/**
	 * The string representation of this group.
	 */
	public String toString() {
		return name;
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
		for (int i = 0; i < ulist.size(); i++) {
			if (ulist.get(i).compareTo(u) == 0) {
				return ulist.get(i);
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
		ulist.add(n);
		return n;
	}
}
