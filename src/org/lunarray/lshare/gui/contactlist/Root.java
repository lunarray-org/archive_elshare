package org.lunarray.lshare.gui.contactlist;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * The root node of a model.
 * @author Pal Hargitai
 */
public class Root {
	
	/**
	 * The group of online buddies.
	 */
	private Group buddies;
	
	/**
	 * The group of online users.
	 */
	private Group online;

	/**
	 * The group of offline buddies.
	 */
	private Group offline;
	
	/**
	 * The instance of the protocol that is accessed by this node.
	 */
	private LShare lshare;
	
	/**
	 * Instanciates the node and it's direct children.
	 * @param ls The instance of the protocol to use.
	 */
	public Root(LShare ls) {
		lshare = ls;
		buddies = new Group("Online Buddies");
		online = new Group("Online Users");
		offline = new Group("Offline Buddies");
		initTree();
	}

	/**
	 * Gets the group at a specified index.
	 * @param arg0 The index of the group to get.
	 * @return The group at the index.
	 */
	public Group getChildAt(int arg0) {
		switch (arg0) {
		case 0:
			return buddies;
		case 1:
			return online;
		case 2:
			return offline;
		default:
			return null;
		}
	}

	/**
	 * Gets the amount of children.
	 * @return The amount of children in this node. (Generally 3)
	 */
	public int getChildCount() {
		return 3;
	}

	/**
	 * Gets the index of a specified child.
	 * @param arg0 The object to get the index of.
	 * @return The index of the child.
	 */
	public int getIndex(Object arg0) {
		if (buddies == arg0) {
			return 0;
		} else if (online == arg0) {
			return 1;
		} else if (offline == arg0) {
			return 2;
		} else {
			return -1;
		}
	}

	/**
	 * Gives a string representation of this root.
	 * @return The string representation of this root.
	 */
	public String toString() {
		return "Contact List";
	}

	/**
	 * Populating the groups with nodes.
	 */
	private void initTree() {
		for (User u: lshare.getUserList().getUserList()) {
			if (u.isBuddy()) {
				if (u.isOnline()) {
					buddies.addUser(u);
				} else {
					offline.addUser(u);
				}
			} else {
				if (u.isOnline()) {
					online.addUser(u);
				}
			}
		}
	}
}
