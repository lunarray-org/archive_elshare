package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.search.SearchList;
import org.lunarray.lshare.protocol.state.userlist.User;

public class SearchEvent {

	private RemoteFile entry;
	private SearchList source;
	private User user;
	
	public SearchEvent(RemoteFile e, SearchList s, User u) {
		entry = e;
		source = s;
		user = u;
	}
	
	public SearchList getSource() {
		return source;
	}
	
	public User getUser() {
		return user;
	}
	
	public RemoteFile getEntry() {
		return entry;
	}
}
