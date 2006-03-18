package edu.tue.compnet.protocol.state;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.State;

/**
 * The list of active users.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class UserList {
	// The user list
	CopyOnWriteArrayList<ListUser> userlist;
	// The listeners
	ArrayList<UserListListener> userlistlisteners;
	// The state
	State state;
	
	/**
	 * The constructor for the user list.
	 * @param s The state of the protocol.
	 */
	public UserList(State s) {
		userlist = new CopyOnWriteArrayList<ListUser>();
		userlistlisteners = new ArrayList<UserListListener>();
		state = s;
	}
	
	/**
	 * Add a user to the list. Or refresh his timeout.
	 * @param name The name of the user.
	 * @param address The address of the user.
	 */
	public synchronized void addUserToList(String name, InetAddress address) {
		// Check if the user has had a timeout.
		if (!refreshUser(name, address)) {
			ListUser lu = new ListUser(name, address);
			userlist.add(lu);
			// Notify listeners
			ListEvent e = new ListEvent(state, lu);
			for (UserListListener lis: userlistlisteners) {
				lis.listAddEvent(e);
			}
		}
	}
	
	/**
	 * Get the list of users.
	 * @return An array of users.
	 */
	public synchronized ListUser[] getUserList() {
		ListUser[] list = new ListUser[userlist.size()];
		return userlist.toArray(list);
	}

	/**
	 * Refresh a user, if possible
	 * @param name The name of the user.
	 * @param address The address of the user.
	 * @return True if a refresh has occured, false if not.
	 */
	public synchronized boolean refreshUser(String name, InetAddress address) {
		boolean hasupdated = false;
		search: {
			for (ListUser lu: userlist) {
				if (lu.getAddress().equals(address)) {
					hasupdated = true;
					if (!lu.getName().equals(name)) {
						lu.setName(name);
					}
					lu.resetTimeout();
					// Notify listeners
					ListEvent ev = new ListEvent(state, lu);
					for (UserListListener lis: userlistlisteners) {
						lis.listUpdateEvent(ev);
					}
					break search;
				}
			}
		}
		return hasupdated;
	}
	
	/**
	 * Check wether a timeout on a user has occured.
	 * @param diff The difference since the last update.
	 */
	public synchronized void checkTimeouts(long diff) {
		// Check the user timeouts
		ArrayList<ListUser> toremove = new ArrayList<ListUser>();
		for (ListUser lu: userlist) {
			if (lu.checkTimeout(diff)) {
				// Item has timed out
				toremove.add(lu);
			}
		}
		// Remove the timedout users
		for (ListUser lu: toremove) {
			userlist.remove(lu);
			// Notify listeners
			ListEvent ev = new ListEvent(state, lu);
			for (UserListListener lis: userlistlisteners) {
				lis.listRemoveEvent(ev);
			}
		}
	}
	
	/**
	 * Add a listener to notify when events happen.
	 * @param lis The listener to add.
	 */
	public synchronized void addUserListListener(UserListListener lis) {
		userlistlisteners.add(lis);
	}

	/**
	 * Remove a listener from the list.
	 * @param lis The listener to remove.
	 */
	public synchronized void removeUserListListener(UserListListener lis) {
		userlistlisteners.remove(lis);
	}

	/**
	 * Get a username that goes with an address, if it can't be found, it's
	 * anonymous.
	 * @param address The address to search for.
	 * @return The username that's associated.
	 */
	public synchronized String getUserName(InetAddress address) {
		String username = "anonymous";
		search: {
			for (ListUser lu: getUserList()) {
				if (lu.getAddress().equals(address)) {
					username = lu.getName();
					break search;
				}
			}
		}
		return username;
	}

}
