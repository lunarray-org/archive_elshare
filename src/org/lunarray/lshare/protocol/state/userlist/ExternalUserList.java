package org.lunarray.lshare.protocol.state.userlist;

import java.util.List;

import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;

/**
 * The external itnerface for user list management.
 * @author Pal Hargitai
 */
public interface ExternalUserList {
	
	/**
	 * Get the current userlist.
	 * @return The userlist.
	 */
	List<User> getUserList();

	/**
	 * Adds a listener for user events.
	 * @param lis The listener to add.
	 */
	void addListener(UserListener lis);
	
	/**
	 * Removes a listener for user events.
	 * @param lis The listener to remove.
	 */
	void removeListener(UserListener lis);
	
	/**
	 * Gets the filelist for the specified user.
	 * @param u The user to get the filelist of.
	 * @return The filelist of the specified user.
	 */
	FilelistEntry getFilelist(User u);
}
