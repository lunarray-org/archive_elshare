package org.lunarray.lshare.protocol.state;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.UserEvent;
import org.lunarray.lshare.protocol.events.UserListener;

public class UserList implements ExternalUserList {
	
	private LinkedList<User> userlist;
	private ArrayList<UserListener> listeners;
	private Controls controls;
	
	public UserList(Controls c) {
		userlist = new LinkedList<User>();
		listeners = new ArrayList<UserListener>();
		controls = c;
		initList();
	}
	
	private void initList() {
		for (String k: controls.getSettings().getChallenges()) {
			String h = controls.getSettings().getSavedName(k);
			userlist.add(new User(k, null, h, true, this));
		}
	}
	
	protected void removeBuddy(User u) {
		controls.getSettings().removeBuddy(u.getChallenge());
	}

	protected void addBuddy(User u) {
		controls.getSettings().saveBuddy(u.getName(), u.getChallenge());
	}
	
	private void signon(UserEvent e) {
		Controls.getLogger().fine("UserList: user \"" + e.getUser().getName() +
				"\" logged on " + e.getUser().getHostaddress());
		
		for (UserListener l: listeners) {
			l.signon(e);
		}
	}
	
	private void signoff(UserEvent e) {
		Controls.getLogger().fine("UserList: user \"" + e.getUser().getName() +
				"\" logged off ");
		
		for (UserListener l: listeners) {
			l.signoff(e);
		}
	}	

	private void update(UserEvent e) {
		Controls.getLogger().fine("UserList: user \"" + e.getUser().getName() +
				"\" updated " + e.getUser().getHostaddress());
		
		for (UserListener l: listeners) {
			l.update(e);
		}
	}
	
	private User findUserByChallenge(String challenge) {
		User u = null;
		search: {
			for (User w: userlist) {
				if (w.challengeMatches(challenge)) {
					u = w;
					break search;
				}						
			}
		}
		return u;
	}
	
	private User findUserByAddress(InetAddress a) {
		User u = null;
		search: {
			for (User w: userlist) {
				if (w.getAddress().equals(a)) {
					u = w;
					break search;
				}
			}
		}
		return u;
	}
	
	public synchronized List<User> getUserList() {
		return userlist;
	}
	
	public synchronized void signonUser(String challenge, InetAddress a,
			String name) {
		User u = findUserByAddress(a);
		
		if (u != null) {
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
		} else {
			// Address doesn't exist
			u = findUserByChallenge(challenge);
		
			if (u != null) {
				// Challenge matches
				u.setAddress(a);
				u.setName(name);
			} else {
				// Completely new
				u = new User(challenge, a, name, false, this);
				userlist.add(u);
			}
			signon(new UserEvent(u, this));
		}
	}
	
	public synchronized void signoffUser(InetAddress a) {
		User u = findUserByAddress(a);
		
		if (u != null) {
			if (!u.isBuddy()) {
				userlist.remove(u);
			}
			u.setAddress(null);
			
			signoff(new UserEvent(u, this));
		}
	}

	public synchronized void addListener(UserListener lis) {
		listeners.add(lis);
	}
	
	public synchronized void removeListener(UserListener lis) {
		listeners.remove(lis);
	}
}
