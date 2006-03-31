package org.lunarray.lshare.protocol.state.userlist;

import java.util.List;

import org.lunarray.lshare.protocol.events.UserListener;

public interface ExternalUserList {
	
	public List<User> getUserList();

	public void addListener(UserListener lis);
	
	public void removeListener(UserListener lis);
	
}
