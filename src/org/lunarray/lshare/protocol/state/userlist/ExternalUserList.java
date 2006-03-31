package org.lunarray.lshare.protocol.state.userlist;

import java.util.List;

import org.lunarray.lshare.protocol.events.UserListener;

public interface ExternalUserList {
	
	void addListener(UserListener lis);
	void removeListener(UserListener lis);
	List<User> getUserList();
	
}
