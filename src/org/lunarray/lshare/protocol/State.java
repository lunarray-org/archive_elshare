package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.state.sharing.ShareList;
import org.lunarray.lshare.protocol.state.userlist.UserList;

public class State {
	
	private UserList ulist;
	private ShareList slist;
	
	public State(Controls c) {
		ulist = new UserList(c);
		slist = new ShareList(c);
	}
	
	public void commit() {
		
	}
	
	public UserList getUserList() {
		return ulist;
	}
	
	public ShareList getShareList() {
		return slist;
	}
}
