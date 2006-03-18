package edu.tue.compnet.events;

import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.state.ListUser;

/**
 * This class holds most, if not all, usefull information for a list update
 * event.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class ListEvent extends Event {
	// This user triggered the event
	ListUser user;
	
	/**
	 * The constructor of the list event.
	 * @param s The event associated with it.
	 * @param lu The user that triggered the event.
	 */
	public ListEvent(State s, ListUser lu) {
		super(s, "User event: " + lu.getName());
		user = lu;
	}

	/**
	 * Gets the user associated with the event.
	 * @return The user associated with the event.
	 */
	public ListUser getTriggerUser() {
		return user;
	}
}
