package org.lunarray.lshare.gui.contactlist;

import org.lunarray.lshare.protocol.state.userlist.User;

public class UserNode implements Comparable<User> {
	
	private Group group;
	private User user;
	
	public UserNode(Group g, User u) {
		group = g;
		user = u;
	}
	
	public User getUser() {
		return user;
	}

	public Group getParent() {
		return group;
	}

	public String toString() {
		return user.getName() + " (" + user.getHostname() + ")";
	}
	
	public int compareTo(User arg0) {
		return user.compareTo(user);
	}
	
}
