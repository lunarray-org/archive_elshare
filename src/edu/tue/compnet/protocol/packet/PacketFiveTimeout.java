package edu.tue.compnet.protocol.packet;

import java.net.InetAddress;

import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;
import edu.tue.compnet.protocol.state.Request;
import edu.tue.compnet.protocol.state.TransferFile;

/**
 * When a packet of type 5 is sent, we expect one of 6 to come back. This
 * timeout handler makes sure we atleast try it.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketFiveTimeout extends PacketTimeout {
	// The name of the file requested
	String filename;
	// The address
	InetAddress address;
	// Resend
	PacketFive five;
	// The daemon
	Transport daemon;
	// The state of the protocol
	State state;
	// The transfer file
	TransferFile tfile;
	
	/**
	 * The constructor of the timeout.
	 * @param a The amount of timeouts that may occur.
	 * @param t The time between timeouts.
	 * @param n The name of the file.
	 * @param ad The address of the remote party.
	 * @param pf The packet five so we can resend it.
	 * @param s The state of the protocol.
	 * @param d The daemon to send it through.
	 * @param tf The transfer file associated with the timeout.
	 */
	public PacketFiveTimeout(int a, int t, String n, InetAddress ad,
			PacketFive pf, State s, Transport d, TransferFile tf) {
		super(a, t);
		filename = n;
		address = ad ;
		five = pf;
		daemon = d;
		state = s;
		tfile = tf;
	}

	/**
	 * Locks any relevant variables.
	 */
	public void lock() {
		// Not needed here
	}

	/**
	 * Unlocks any relevant variables.
	 */
	public void unlock() {
		// Not needed here
	}

	/**
	 * Checks wether the timeout itself is still applicable.
	 */
	public boolean isValid() {
		return state.getRequestList().requestExists(filename, address);
	}

	/**
	 * Handles passing of a timeout.
	 */
	public void handleTimeout() {
		daemon.send(five.getPacket());
	}

	/**
	 * Cleans up the timeout if it's not needed anymore.
	 */
	public synchronized void cleanUp() {
		/*state.getRequestList().
			removeRequest(filename, address);*/
		if (state.getRequestList().requestExists(filename, address)) {
			Request req = state.getRequestList().getRequest(filename,
					address);
			tfile.releaseChunk(req.getChunk());
		}
	}
}
