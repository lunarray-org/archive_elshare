package org.lunarray.lshare.protocol.state.userlist;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.UserEvent;
import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;
import org.lunarray.lshare.protocol.filelist.FilelistReceiver;

/**
 * Manages the user list.
 * @author Pal Hargitai
 */
public class UserList implements ExternalUserList {
	
	/**
	 * The current list of users.
	 */
	private LinkedList<User> userlist;
	
	/**
	 * The listeners for user events.
	 */
	private ArrayList<UserListener> listeners;
	
	/**
	 * The controls to the protocol.
	 */
	private Controls controls;
	
	/**
	 * Constructs a user list.
	 * @param c The controls to the protocol.
	 */
	public UserList(Controls c) {
		userlist = new LinkedList<User>();
		listeners = new ArrayList<UserListener>();
		controls = c;
		initList();
	}
	
	/**
	 * Find a specified user by his challenge.
	 * @param challenge The challenge to find the user by.
	 * @return The user associated with this challenge, or null.
	 * @throws UserNotFound Thrown if the user cannot be found.
	 */
	public User findUserByChallenge(String challenge) throws UserNotFound {
		for (User w: userlist) {
			if (w.challengeMatches(challenge)) {
				return w;
			}						
		}
		throw new UserNotFound();
	}
	
	/**
	 * Find a user by his address.
	 * @param a The address to find the user by.
	 * @return The user with the specified address.
	 * @throws UserNotFound Thrown if the user cannot be found.
	 */
	public User findUserByAddress(InetAddress a) throws UserNotFound {
		for (User w: userlist) {
			if (w.getAddress() != null) {
				if (w.getAddress().equals(a)) {
					return w;
				}
			}
		}
		throw new UserNotFound();
	}
	
	/**
	 * Get the userlist.
	 * @return The current userlist.
	 */
	public synchronized List<User> getUserList() {
		return userlist;
	}
	
	/**
	 * Handle a user signon.
	 * @param challenge The challenge of the user.
	 * @param a The address of the user.
	 * @param name The name of the user.
	 */
	public synchronized void signonUser(String challenge, InetAddress a,
			String name) {
		try {
			User u = findUserByAddress(a);
			
			// Address exists
			if (u.challengeMatches(challenge)) {
				// Challenge and address match
				u.bump();
				if (!u.getName().equals(name)) {
					u.setName(name);
					update(new UserEvent(u, this));
				}
			} else {
				// Challenge doesnt' match
				if (!u.isBuddy()) {
					userlist.remove(u);
				}
				u.setAddress(null);
				signoff(new UserEvent(u, this));
				
				u = findUserByChallenge(challenge);
				if (u != null) {
					u.setAddress(a);
					u.setName(name);
				} else {
					u = new User(challenge, a, name, false, this);
					userlist.add(u);
				}
				signon(new UserEvent(u, this));
			}
		} catch (UserNotFound unf) {
			// Address doesn't exist
			User u;
			try {
				u = findUserByChallenge(challenge);
				u.setAddress(a);
				u.setName(name);
			} catch (UserNotFound un) {
				u = new User(challenge, a, name, false, this);
				userlist.add(u);
			}
			signon(new UserEvent(u, this));
		}
	}
	
	/**
	 * Handle a user signoff.
	 * @param a The address of the user to sign off.
	 */
	public synchronized void signoffUser(InetAddress a) {
		
		try {
			User u = findUserByAddress(a);
		
			if (!u.isBuddy()) {
				userlist.remove(u);
			}
			u.setAddress(null);
			
			signoff(new UserEvent(u, this));
		} catch (UserNotFound ufn) {
			// User wasn't signed on in the first place.
		}
	}

	/**
	 * Adds a listener for user events.
	 * @param lis The listener to register for user events.
	 */
	public synchronized void addListener(UserListener lis) {
		listeners.add(lis);
	}

	/**
	 * Removes a listener for user events.
	 * @param lis The listener to unregister from user events.
	 */
	public synchronized void removeListener(UserListener lis) {
		listeners.remove(lis);
	}
	
	/**
	 * Get the specified users file list.
	 * @param u The user to get the filelist of.
	 * @return The users filelist.
	 */
	public FilelistEntry getFilelist(User u) {
		FilelistReceiver flr = new FilelistReceiver(u);
		return flr.getRoot();
	}
	
	/**
	 * Unregister a user as a buddy.
	 * @param u The user to unbuddy.
	 */
	protected void removeBuddy(User u) {
		controls.getSettings().getUserSettings().removeBuddy(u.getChallenge());
		
		update(new UserEvent(u, this));
	}

	/**
	 * Registers a user as a buddy.
	 * @param u The user to buddify.
	 */
	protected void addBuddy(User u) {
		controls.getSettings().getUserSettings().saveBuddy(u.getName(),
				u.getChallenge());
		
		update(new UserEvent(u, this));
	}
	
	/**
	 * Inits the user list
	 */
	private void initList() {
		for (String k: controls.getSettings().getUserSettings().
				getChallenges()) {
			String h = controls.getSettings().getUserSettings().
					getSavedName(k);
			userlist.add(new User(k, null, h, true, this));
		}
	}
	
	/**
	 * Trigger listeners with a signon.
	 * @param e The event to give the listeners.
	 */
	private void signon(UserEvent e) {
		Controls.getLogger().fine("User \"" + e.getUser().getName() +
				"\" logged on " + e.getUser().getHostaddress());
		
		for (UserListener l: listeners) {
			l.signon(e);
		}
	}
	
	/**
	 * Trigger listeners with a signoff.
	 * @param e The event to give the listeners.
	 */
	private void signoff(UserEvent e) {
		Controls.getLogger().fine("User \"" + e.getUser().getName() +
				"\" logged off ");
		
		for (UserListener l: listeners) {
			l.signoff(e);
		}
	}	

	/**
	 * Trigger listeners with an update.
	 * @param e The event to give the listeners.
	 */
	private void update(UserEvent e) {
		Controls.getLogger().fine("User \"" + e.getUser().getName() +
				"\" updated " + e.getUser().getHostaddress());
		
		for (UserListener l: listeners) {
			l.update(e);
		}
	}
}
