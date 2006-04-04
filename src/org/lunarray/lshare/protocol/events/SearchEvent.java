package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.search.SearchList;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A search event that should be sent to searchlisteners.
 * @author Pal Hargitai
 */
public class SearchEvent {

	/**
	 * The file entry that comes with this event.
	 */
	private RemoteFile entry;
	
	/**
	 * The search list that triggered the events.
	 */
	private SearchList source;
	
	/**
	 * The user that this event comes from.
	 */
	private User user;
	
	/**
	 * Constructs the searchevent.
	 * @param e The file that comes with this event.
	 * @param s The source of this event, the searchlist.
	 * @param u The user that comes with this event.
	 */
	public SearchEvent(RemoteFile e, SearchList s, User u) {
		entry = e;
		source = s;
		user = u;
	}
	
	/**
	 * Gets the searchlist that triggered this event.
	 * @return The list.
	 */
	public SearchList getSource() {
		return source;
	}
	
	/**
	 * Gets the user that this entry belongs to.
	 * @return The user.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * The remote file entry that comes with this event.
	 * @return The remote entry.
	 */
	public RemoteFile getEntry() {
		return entry;
	}
}
