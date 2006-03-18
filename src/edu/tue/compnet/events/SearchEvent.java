package edu.tue.compnet.events;

import java.net.InetAddress;

import edu.tue.compnet.protocol.State;

/**
 * The event that comes with a search, holds data about what results have been
 * received.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class SearchEvent extends Event {
	// The name of the result.
	String name;
	// The size of the result.
	int size;
	// The date of the result.
	int date;
	// The address of the source.
	InetAddress address;
	// The nickname of the source.
	String nick;
	// The hash of the file
	byte[] hash;
	
	/**
	 * Creates a search result event.
	 * @param state The state the protocol is in
	 * @param a The address of the sender.
	 * @param i The nickname of the sender.
	 * @param n The name of the result.
	 * @param s The size of the result file.
	 * @param d The last modified date of the result file.
	 * @param h The hash of the file.
	 */
	public SearchEvent(State state, InetAddress a, String i, String n, int s,
			int d, byte[] h) {
		super(state, "Search result.");
		name = n;
		size = s;
		date = d;
		address = a;
		nick = i;
		hash = h;
	}
	
	/**
	 * Get the internet address for the user that triggered this event.
	 * @return The address
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Get the nickname for the user that triggerd this event.
	 * @return The nickname
	 */
	public String getUserName() {
		return nick;
	}
	
	/**
	 * Gets the size of the file.
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the date of the file.
	 * @return The date.
	 */
	public int getDate() {
		return date;
	}
	
	/**
	 * Gets the name of the file.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the hash of the file, if it exists.
	 * @return The hash.
	 */
	public byte[] getHash() {
		return hash;
	}
}
