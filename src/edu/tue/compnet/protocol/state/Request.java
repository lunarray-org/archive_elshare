package edu.tue.compnet.protocol.state;

import java.net.InetAddress;

/**
 * A request of a file from an address.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Request {
	// The name of the file
	String fname;
	// The address of the user
	InetAddress address;
	// The size of the file requested and the offset
	int offset;
	int size;
	// The chunk associated with the request
	Chunk chunk;
	
	/**
	 * Constructs the request.
	 * @param n The name of the file requested.
	 * @param a The address it is requested from.
	 * @param o The offset of the file requested.
	 * @param s The size of the file requested.
	 */
	public Request(String n, InetAddress a, int o, int s) {
		fname = n;
		address = a;
		offset = o;
		size = s;
	}
	
	/**
	 * Sets the chunk that's associated with this request.
	 * @param c The chunk requested.
	 */
	public void setChunk(Chunk c) {
		chunk = c;
	}
	
	/**
	 * Gets the chunk associated with this request.
	 * @return The chunk.
	 */
	public Chunk getChunk() {
		return chunk;
	}
	
	/**
	 * The offset of the mark of the file requested.
	 * @return The offset.
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * The size of the requested file.
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Checks wether a request equals another.
	 * @param r The other request to compare it to.
	 * @return True if the request equals another, false if not.
	 */
	public boolean equals(Request r) {
		//return fname.equals(r.fname) && address.equals(r.address);
		// We'll just sort on address, easier to handle buddylist this way.
		return address.equals(r.address);
	}
}

