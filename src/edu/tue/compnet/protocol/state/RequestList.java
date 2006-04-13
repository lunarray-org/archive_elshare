package edu.tue.compnet.protocol.state;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import edu.tue.compnet.Output;

/**
 * The request list, handles, searches and generally takes care of request
 * list related issues.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class RequestList {
	// The file requests
	ArrayList<Request> requests;

	/**
	 * Constructs a request list.
	 */
	public RequestList() {
		requests = new ArrayList<Request>();
	}
	
	/**
	 * Register a file request.
	 * @param fn The file name.
	 * @param a The address of the remote host.
	 * @param offset The offset of the request
	 * @param size The size of the requested file.
	 * @param c The chunk locked for this request
	 */
	public synchronized void registerRequest(String fn, InetAddress a, int offset, int 
			size, Chunk c) {
		Request r = new Request(fn, a, offset, size);
		requests.add(r);
		r.setChunk(c);
	}
	
	/**
	 * Gets the request that matches these parameters.
	 * @param fn The filename of the request.
	 * @param a The address it's requested from.
	 * @return The request that matches these.
	 */
	public synchronized Request getRequest(String fn, InetAddress a) {
		Request re = new Request(fn, a, 0, 0);
		Request ret = null;
		search: {
			try {
				for (Request r: requests) {
					if (r.equals(re)) {
						ret = r;
						break search;
					}
				}
			} catch (ConcurrentModificationException cme) {
				Output.err("Concurrent modification! (3)");
			}
		}
		return ret;
	}
	
	/**
	 * Remove a request, ie. timeout or handled
	 * @param fn The filename.
	 * @param a The address.
	 * @return Wether it was there.
	 */
	public synchronized boolean removeRequest(String fn, InetAddress a) {
		ArrayList<Request> toremove = new ArrayList<Request>();
		Request re = new Request(fn, a, 0, 0);
		boolean isremoved = false;
		try {
			for (Request r: requests) {
				if (r.equals(re)) {
					toremove.add(r);
					isremoved = true;
				}
			}
			for (Request r: toremove) {
				requests.remove(r);
			}
		} catch (ConcurrentModificationException cme) {
			Output.err("Concurrent modification! (3)");
		}
		return isremoved;
	}
	
	/**
	 * Check wether a request exists.
	 * @param fn The filename.
	 * @param a The address.
	 * @return True if a request of the sort exists, false if not.
	 */
	public synchronized boolean requestExists(String fn, InetAddress a) {
		boolean ret = false;
		search: {
			Request re = new Request(fn, a, 0, 0);
			for (Request r: requests) {
				if (r.equals(re)) {
					ret = true;
					break search;
				}
			}
		}
		return ret;
	}
}
