package edu.tue.compnet.protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimerTask;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.packet.PacketFive;
import edu.tue.compnet.protocol.packet.PacketOne;
import edu.tue.compnet.protocol.packet.PacketSeven;
import edu.tue.compnet.protocol.packet.PacketThree;

/**
 * Handles regular tasks done in the protocol.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Tasks {
	
	/** The interval at which type one packet should be sent. */
	public static int INTERVAL = 30000;
	/** The interval at which timeouts should be checked. */
	public static int TIMEOUT_CHECKS = 500;
	/** The broadcast address string. */
	public static String BROADCAST = "255.255.255.255";
	
	// The transport
	Transport transport;
	// The state
	State state;
	// The packet one task
	PacketOneTask potask;
	// Timeout checker
	TimeoutChecker totask;
	// List checker
	TimerTask checker;
	long lastcheck;

	/**
	 * Constructs the tasks of the protocol.
	 * @param s The state of the protocol
	 */
	public Tasks(State s) {
		lastcheck = System.currentTimeMillis();
		
		state = s;
		potask = new PacketOneTask();
		totask = new TimeoutChecker();
	}
	
	/**
	 * Set the transport to be used.
	 * @param t
	 */
	public void setTransport(Transport t) {
		transport = t;	
	}
	
	/**
	 * Initialise the basic takss of the protocol.
	 */
	public void init() {
		potask.run();
		state.addProcotolTask(potask, Tasks.INTERVAL);
		state.addProcotolTask(totask, TIMEOUT_CHECKS);
	}
	
	/**
	 * Handles the creation and sending of a search.
	 * @param filename The filename to search for.
	 * @param h The hash to search for.
	 */
	public void searchFor(String filename, byte[] h) {
		try {
			PacketThree pt = new PacketThree(filename, InetAddress.getByName(
					BROADCAST), Transport.PORT, Transport.MTU, h);
			transport.send(pt.getPacket());
			Output.out("Packet type three enqueued!");
		} catch (UnknownHostException uhe) {
			Output.err("Error getting address!");
		}
	}
	
	/**
	 * Handles sending a file request.
	 * @param filename The filename to request.
	 * @param address The address to request it at.
	 * @param size The size of the file.
	 * @return Wether the file request has been sent.
	 */
	public boolean requestFile(String filename, InetAddress address, int
			size) {
		PacketFive pf = new PacketFive(filename, address, Transport.PORT,
				Transport.MTU, state, transport, size);
		if (pf.isValid()) {
			transport.send(pf.getPacket());
			Output.out("Packet type five enqueued!");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Requests a file list from a user
	 * @param address The address to send the request to.
	 */
	public void requestFilelist(String address) {
		try {
			InetAddress a = InetAddress.getByName(address);
			PacketSeven ps = new PacketSeven(a, Transport.PORT, state,
					transport);
			transport.send(ps.getPacket());
			Output.out("Packet type seven enqueued!");
		} catch (UnknownHostException e) {
			Output.out("Unknown host!");
		}
	}
	
	/**
	 * This class runs a packet one broadcast every 60 seconds.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class PacketOneTask extends TimerTask {
		/**
		 * This function queues the packet one for sending.
		 */
		public void run() {
			try {
				PacketOne one = new PacketOne(InetAddress.getByName(
						BROADCAST), Transport.PORT, state.
						getStateSettings().getNickname(), Transport.MTU);
				transport.send(one.getPacket());
				Output.out("Packet type one enqueued!");
			} catch (UnknownHostException uhe) {
				Output.err("Error getting address!");
			}
		}
	}
	
	/**
	 * The task to check for timeouts in the userlist.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class TimeoutChecker extends TimerTask {
		/**
		 * Checks the timeouts.
		 */
		public void run() {
			/*
			 * update timeout and delete from list if need be.
			 * First calculate new times, then update.
			 */
			long newtime = System.currentTimeMillis();
			long diff = newtime - lastcheck;
			lastcheck = newtime;
			state.getPacketTimeoutList().checkTimeouts(diff);
			// Check wether filetransfers are still active
			state.getFiletransferList().checkTransfers();
			state.getUserList().checkTimeouts(diff);
		}
	}
}
