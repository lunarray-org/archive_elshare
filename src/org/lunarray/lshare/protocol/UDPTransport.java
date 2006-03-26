package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.lunarray.lshare.protocol.packets.InvalidPacket;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.SignOffIn;
import org.lunarray.lshare.protocol.packets.SignOnIn;

public class UDPTransport extends Thread {
	/** The listen timeout. */
	public static int LISTEN_TO = 5;
	
	
	// Set to true to listen, false to stop listening
	private boolean run;
	// The UDP socket
	private DatagramSocket socket;
	// The packets to send
	private LinkedBlockingQueue<DatagramPacket> sendlist;
	// Synchronisation primitive for shutdown
	private Semaphore shutdownsem;
	// Tasks class for handling incoming packets
	private Controls controls;



	public UDPTransport(Controls c) {
		sendlist = new LinkedBlockingQueue<DatagramPacket>();
		shutdownsem = new Semaphore(1);
		run = false;
		controls = c;
	}

	public void init() {
		try {
			socket = new DatagramSocket(Controls.UDP_PORT);
			socket.setSoTimeout(LISTEN_TO);
		} catch (SocketException se) {
			// Something has gone awefully wrong
			Controls.getLogger().severe("UDPTransport: cannot bind to " +
					"socket on port: " + Integer.valueOf(Controls.UDP_PORT).
					toString() + " (" + se.getMessage() + ")");
		}
		// Start running
		run = true;
		start();
	}
	
	public void run() {
		run: {
			try {
				shutdownsem.acquire();
			} catch (InterruptedException ie) {
				Controls.getLogger().severe("UDPTransport: shutdown " +
						"initiated before run!");
				break run;
			}		
			if (socket != null) {
				if (socket.isBound()) {
					while (true) {
						DatagramPacket packet = new DatagramPacket(new
								byte[Controls.UDP_MTU], Controls.UDP_MTU);
						try {
							socket.receive(packet);
							String type;
							PacketIn inpack;
							if (SignOffIn.isType(packet.getData())) {
								type = "SignOff";
								inpack = new SignOffIn(packet);
							} else if (SignOnIn.isType(packet.getData())) {
								type = "SignOn";
								inpack = new SignOnIn(packet);
							} else {
								type = "";
								inpack = new InvalidPacket();
							}
							Controls.getLogger().fine(type + " " +
									"received from: " + packet.getAddress().
									getHostAddress());
							try {
								inpack.parse();
								controls.getTasks().enqueueSingleTask(inpack);
							} catch (MalformedPacketException mpe) {
								Controls.getLogger().warning(
										"Malformed packet received " +
										"from: " + packet.getAddress().
										getHostAddress());
							}
						} catch (IOException ie) {
							// This is normal.
						}
						// Send all sockets in queue
						while (sendlist.peek() != null) {
							DatagramPacket send = null;
							try {
								send = sendlist.take();
								socket.send(send);
								Controls.getLogger().fine("UDPTransport: " +
										"send packet to " + send.getAddress().
										getHostAddress() + " on " + Integer.
										valueOf(send.getPort()).toString());
							} catch (InterruptedException ie) {
								// This isn't going to happen
							} catch (IOException ie) {
								Controls.getLogger().severe(
										"UDPTransport: unable to send " +
										"packet to: " + send.getAddress().
										getHostAddress());
							}
						}
						if (!run) {
							break run;
						}
					}
				}
			}
		}
		shutdownsem.release();
	}
	
	public void close() {
		run = false;
		try {
			shutdownsem.acquire();
		} catch (InterruptedException ie) {
			Controls.getLogger().severe("UDPTransport: Shutdown " +
					"interrupted!");
		}
		if (socket != null) {
			socket.close();
		}
	}
	
	public void send(DatagramPacket p) {
		try {
			sendlist.put(p);
		} catch (InterruptedException ie) {
			Controls.getLogger().fine("UDPTransport: sendlist put " +
					"timeout! (" + ie.getMessage() + ")");
		}
	}
}
