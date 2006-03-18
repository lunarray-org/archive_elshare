package edu.tue.compnet.protocol.packet;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;

/**
 * This means that when a timeout occurs, the packet 6, advertising the
 * port will be resent. If it runs out of timeouts, it closes the file
 * transfer server.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */ 
public class PacketFiveHandleTimeout extends PacketTimeout {
	// The packet of type 6 to transfer
	PacketSix six;
	// The daemon
	Transport daemon;
	// The file transfer
	FiletransferServer server;
	// The state of the protocol
	State state;
	
	/**
	 * The constructor of a timeout handler for handling a packet of type 5.
	 * @param a The amount of timeouts.
	 * @param t The timeout time.
	 * @param ps The original packet 6.
	 * @param d The daemon for sending.
	 * @param fs The file transfer to close.
	 * @param s The state of the protocol.
	 */
	public PacketFiveHandleTimeout(int a, int t, PacketSix ps, Transport d,
			FiletransferServer fs, State s) {
		super(a, t);
		six = ps;
		daemon = d;
		server = fs;
		state = s;
	}
	
	/**
	 * Lock the server.
	 */
	public void lock() {
		server.lock();
	}
	
	/**
	 * Unlock the server.
	 */
	public void unlock() {
		server.unlock();
	}
	
	/**
	 * Timeout is valid if the server isn't running.
	 */
	public boolean isValid() {
		return !server.isRunning();
	}

	/**
	 * If the server isn't running although it should, resend packet.
	 */
	public void handleTimeout() {
		Output.out("Timeout on connection!");
		// Check if the filetransfer is going otherwise, just handle timeout.
		if (!server.isRunning() && server.shouldRun()) {
			// Handle the timeout, thus send a new package of type 6.
			daemon.send(six.getPacket());
		}
	}

	/**
	 * Timeouts have passed, clean up.
	 */
	public void cleanUp() {
		// Clean up, thus close server.
		server.stop();
	}

}
