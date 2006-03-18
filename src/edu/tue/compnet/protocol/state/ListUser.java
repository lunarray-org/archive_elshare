package edu.tue.compnet.protocol.state;

import java.net.InetAddress;

/**
 * A list user. Used to keep an up to date list of active users.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class ListUser {
	/** The time needed for a user to timeout. */
	public long MAX_TIMEOUT = 60000;
	
	// The name of the user
	String name;
	// The address of the user
	InetAddress address;
	// The timeout of the user, in milliseconds
	long timeout;
	
	
	/**
	 * Get the users address.
	 * @return The address of the user.
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Gets the nickname of the user.
	 * @return The name of the user.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the user.
	 * @param n The name it is to be set to.
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * The constructor of the user for the list.
	 * @param n The nickname of the user.
	 * @param a The address of the user.
	 */
	public ListUser(String n, InetAddress a) {
		name = n;
		address = a;
		timeout = MAX_TIMEOUT;
	}
	
	/**
	 * Resets the timeout of the user.
	 */
	public void resetTimeout() {
		timeout = MAX_TIMEOUT;
	}
	
	/**
	 * Update timeout and check for timeout passing.
	 * @param modifier The offset to update with.
	 * @return Wether the timeout has passed
	 */
	public boolean checkTimeout(long modifier) {
		timeout = timeout - modifier;
		return (timeout < 0);
	}
}
