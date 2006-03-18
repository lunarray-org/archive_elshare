package edu.tue.compnet.events;

/**
 * Listens for updates to the user list.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface UserListListener {
	/**
	 * A user has been added to the list event.
	 * @param e The event that gets triggered.
	 */
	void listAddEvent(ListEvent e);
	
	/**
	 * A user has been removed from the list event.
	 * @param e The event that gets triggered.
	 */
	void listRemoveEvent(ListEvent e);

	/**
	 * An update has occured (rename).
	 * @param e The event that gets triggered.
	 */
	void listUpdateEvent(ListEvent e);
}
