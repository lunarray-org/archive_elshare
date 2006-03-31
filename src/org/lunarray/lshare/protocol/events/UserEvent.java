package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserList;

public class UserEvent {
	
	private User user;
	private UserList ulist;
	
	public UserEvent(User u, UserList l) {
		user = u;
		ulist = l;
	}

	public User getUser() {
		return user;
	}
	
	public UserList getUserList() {
		return ulist;
	}
}
