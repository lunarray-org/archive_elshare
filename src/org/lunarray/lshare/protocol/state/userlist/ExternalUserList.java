package org.lunarray.lshare.protocol.state.userlist;

import java.util.List;

import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;

public interface ExternalUserList {
	
	List<User> getUserList();

	void addListener(UserListener lis);
	
	void removeListener(UserListener lis);
	
	FilelistEntry getFilelist(User u);
}
