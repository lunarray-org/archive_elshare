package edu.tue.compnet.protocol;

import java.net.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.packet.*;


/**
 * This class will handle all networking events, it will effectively make sure
 * that the protocol gets properly implemented.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Handler {
	// A threadsafe queue
	LinkedBlockingQueue<DatagramPacket> todolist;
	// The daemon
	Transport daemon;
	// The protocol state
	State state;
	// The timer for timed events
	TimerTask time;
	// Info for the thread
	boolean handlerrun;
	Thread handler;


	/**
	 * The constructor for the handler of the network events.
	 */
	public Handler(Transport network, State s) {
		todolist = new LinkedBlockingQueue<DatagramPacket>();
		state = s;
		daemon = network;
		handler = new HandlerThread();
	}
	
	/**
	 * Start the handler
	 */
	public void startHandler() {
		handlerrun = true;
		handler.start();
	}
	
	/**
	 * Handles a packet
	 * @param packet The packet to be handled.
	 */
	public void handle(DatagramPacket packet) {
		try {
			todolist.put(packet);
		} catch (InterruptedException ie) {
			Output.err("Queue is full!");
		}
	}
	
	/**
	 * Closes the handler
	 */
	public void close() {
		handlerrun = false;
	}
	
	/**
	 * The thread that handles the packets.
	 */
	public class HandlerThread extends Thread {
		/**
		 * The process that runs.
		 */
		public void run() {
			while (handlerrun) {
				DatagramPacket packet = null;
				try {
					packet = todolist.take();
				} catch (InterruptedException ie) {
					Output.err("Interrupted wait for packet to handle!");
				}

				/*
				 * Pick how the packet should be handled depending on 
				 * what the first byte is.
				 */
				if (packet != null) {
					byte[] data = packet.getData();
					try {
						Output.out("Packet received from: " + packet.
								getAddress().getHostAddress());
						switch (data[0]) {
						case 0x01:
							PacketOne.handlePacket(packet, state, daemon);
							break;
						case 0x02:
							PacketTwo.handlePacket(packet, state, daemon);
							break;
						case 0x03:
							PacketThree.handlePacket(packet, state, daemon);
							break;
						case 0x04:
							PacketFour.handlePacket(packet, state, daemon);
							break;
						case 0x05:
							PacketFive.handlePacket(packet, state, daemon);
							break;
						case 0x06:
							PacketSix.handlePacket(packet, state, daemon);
							break;
						case 0x07:
							PacketSeven.handlePacket(packet, state, daemon);
							break;
						case 0x08:
							PacketEight.handlePacket(packet, state, daemon);
							break;
						default:
							Output.err("Invalid packet received from: ");
							Output.err(packet.getAddress());
						}
					} catch (Exception ioe) {
						Output.err("An exception has occured!");
					}
				}
			}
		}
	}
}
