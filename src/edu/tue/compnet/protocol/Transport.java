package edu.tue.compnet.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import edu.tue.compnet.Output;


/**
 * This daemon runs the protocol. It will handle all packets and it will
 * trigger respective events.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Transport {
	/** The port to bind and connect to. */
	public static int PORT = 1337;
	/** The listen timeout. */
	public static int LISTEN_TO = 5;
	/** The maximum transferrable units. */
	public static int MTU = 1400;

	// The event handler
	Handler eventhandler;
	// The UDP socket
	DatagramSocket socket;
	// The main network thread
	Thread mainlisten;
	boolean runlistener;
	// The protocol state
	State state;
	// The packets to send
	LinkedBlockingQueue<DatagramPacket> todolist;

	/**
	 * Constructor of the class, will also construct the event listener.
	 */
	public Transport(State s) {
		todolist = new LinkedBlockingQueue<DatagramPacket>();
		/*
		 * Create an event listener to handle all stuff that is done here.
		 */
		try {
			socket = new DatagramSocket(Transport.PORT);
			socket.setSoTimeout(Transport.LISTEN_TO);
		} catch (SocketException se) {
			// Something has gone awefully wrong
			Output.err("A socket exception has occured!");
		}
		// Get the rest going
		state = s;
		eventhandler = new Handler(this, state);
		// Get the tasks going
		mainlisten = new NetworkHandler();
	}
	
	/**
	 * The network handling class thread.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class NetworkHandler extends Thread {
		/**
		 * The running process.
		 */
		public void run() {
			while (runlistener) {
				// Just listen for an event, and kick it to the handler
				if (socket != null) {
					if (socket.isBound()) {
						DatagramPacket packet = new DatagramPacket(new
								byte[Transport.MTU], Transport.MTU);
						try {
							socket.receive(packet);
							eventhandler.handle(packet);
						} catch (IOException ie) {
							/*
							 * Output.err("An exception has occured while" +
							 * 		"receiving a packet");
							 */
							// With the socket timeout, this is not relevant.
						}
						// Packet seems to be ok, just handle it
					} else {
						bind();
					}
					// Send all sockets in queue
					while (todolist.size() > 0) {
						try {
							DatagramPacket packet = todolist.take();
							socket.send(packet);
						} catch (IOException ie) {
							Output.err("An error has occurred");
						} catch (InterruptedException ie) {
							Output.err("Send interrupted!");
						}
					}
				}
			}
		}
	}
	
	/**
	 * Try to bind to  a good port
	 */
	private void bind() {
		if (socket != null) {
			if (!socket.isBound()) {
				try {
					//socket.bind(new InetSocketAddress(Transport.PORT));
					InetSocketAddress a = new InetSocketAddress(Transport.
							PORT);
					socket.bind(a);
				} catch (SocketException se) {
					Output.err("An error has occured while binding to a " +
							"socket!");
				}
			}
		}
	}
	
	/**
	 * Starts listening to network data
	 */
	public void startDaemon() {
		/*
		 * listen to network stuff here
		 * start the thread and say it should run
		 */
		bind();
		runlistener = true;
		mainlisten.start();
		eventhandler.startHandler();
	}
	
	/**
	 * Tries to clean up and quit everything
	 */
	public void quit() {
		// Close the settings
		state.getSettings().quit();
		// Close the rest
		runlistener = false;
		mainlisten.interrupt();
		// Make the network handler play nice
		// Also close the scoket
		if (socket != null) {
			socket.close();
		}
		// Stop all file transfers
		for (FiletransferServer f: state.getFiletransferList().
				getServerFiletransfers()) {
			f.stop();
		}
		// Close files
		state.getTransferFileList().close();
	}
	
	/**
	 * Sends a packet
	 * @param packet Queues a packet to be sent
	 */
	public void send(DatagramPacket packet) {
		try {
			todolist.put(packet);
		} catch (InterruptedException ie) {
			Output.err("Exception while adding packet to send list");
		}
	}
}
