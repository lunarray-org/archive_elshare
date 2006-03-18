package edu.tue.compnet.events;

import edu.tue.compnet.protocol.State;

/**
 * This class holds all important information regarding a protocol event.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Event {
	// The state of the protocol
	State state;
	// The message
	String message;
	
	/**
	 * The constructor for an event
	 * @param s The state of the protocol.
	 * @param m The message to be sent.
	 */
	public Event(State s, String m) {
		state = s;
		message = m;
	}
	
	/**
	 * Get the message that came with this event.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the protocol state.
	 * @return The protocol state.
	 */
	public State getState() {
		return state;
	}
}
