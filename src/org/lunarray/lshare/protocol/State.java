package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.state.UserList;

public class State {
	
	private UserList ulist;
	
	public State(Controls c) {
		ulist = new UserList(c);
	}
	
	public void commit() {
		
	}
	
	public UserList getUserList() {
		return ulist;
	}
	
}
