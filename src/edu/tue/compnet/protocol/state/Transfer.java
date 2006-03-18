package edu.tue.compnet.protocol.state;

import java.net.InetAddress;
import java.util.List;

/**
 * A transfer so that download/upload screens know better what they're
 * dealing with.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Transfer {
	// The amount todo
	int todo;
	// The total length
	int length;
	// The name
	String name;
	// The rate
	int rate;
	// The address of the user
	List<InetAddress> address;
	
	/**
	 * Sets the address of the remote party of this transfer.
	 * @param a The new address.
	 */
	public void setAddress(List<InetAddress> a) {
		address = a;
	}
	
	/**
	 * Get the address of the remote party of this transfer.
	 * @return The address of the user.
	 */
	public List<InetAddress> getAddress() {
		return address;
	}
	
	/**
	 * Sets the name of this transfer.
	 * @param n The new name.
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Gets the name of the file that's being transferred.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the amount that's to be done.
	 * @param t The new amount.
	 */
	public void setTodo(int t) {
		todo = t;
	}
	
	/**
	 * Gets the amount that has to be transferred.
	 * @return The amount to transfer.
	 */
	public int getTodo() {
		return todo;
	}
	
	/**
	 * Sets the total length of the file
	 * @param l The new length.
	 */
	public void setLength(int l) {
		length = l;
	}
	
	/**
	 * Gets the total length of the file.
	 * @return The total length.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Sets the rate.
	 * @param r The new rate
	 */
	public void setRate(int r) {
		rate = r;
	}
	
	/**
	 * The rate set to this transfer.
	 * @return The rate.
	 */
	public int getRate() {
		return rate;
	}
}
