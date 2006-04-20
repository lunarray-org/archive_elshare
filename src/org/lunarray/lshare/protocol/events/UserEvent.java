package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserList;

/** The user event that is sent to user listeners.
 * @author Pal Hargitai
 */
public class UserEvent {
	/** The user associated with this event.
	 */
	private User user;
	
	/** The userlist that triggerd this event.
	 */
	private UserList ulist;
	
	/** Constructs a user event.
	 * @param u The user associated with this event.
	 * @param l The source that triggered this event.
	 */
	public UserEvent(User u, UserList l) {
		user = u;
		ulist = l;
	}

	/** Gets the user associated with this event.
	 * @return The user.
	 */
	public User getUser() {
		return user;
	}
	
	/** Gets the source of this event.
	 * @return The source that triggered this event.
	 */
	public UserList getUserList() {
		return ulist;
	}
}
