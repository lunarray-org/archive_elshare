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
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.download.RequestIn;
import org.lunarray.lshare.protocol.packets.download.ResponseIn;
import org.lunarray.lshare.protocol.packets.search.ResultIn;
import org.lunarray.lshare.protocol.packets.search.SearchIn;
import org.lunarray.lshare.protocol.packets.user.SignOffIn;
import org.lunarray.lshare.protocol.packets.user.SignOnIn;

/** The UDP transport to send and receive packets.
 * @author Pal Hargitai
 */
public class UDPTransport extends Thread {
	/** The listen timeout. This is {@value} seconds.	 
	 */
	public final static int LISTEN_TO = 5;
	
	/** Manages the running of the transport.
	 */
	private boolean run;
	
	/** The UDP socket for sending and receiving.
	 */
	private DatagramSocket socket;
	
	/** The list of packets to be sent.
	 */
	private LinkedBlockingQueue<DatagramPacket> sendlist;
	
	/** The semaphore for synchronising the shutdown.
	 */
	private Semaphore shutdownsem;
	
	/** The controls for communicating with the rest of the protocol.
	 */
	private Controls controls;

	/** Constructs the UDP transport.
	 * @param c The controls for the protocol.
	 */
	public UDPTransport(Controls c) {
		super(c.getThreadGroup(), "udptransport");
		sendlist = new LinkedBlockingQueue<DatagramPacket>();
		shutdownsem = new Semaphore(1);
		run = false;
		controls = c;
	}

	/** Initialises the transport and makes it run.
	 */
	public void init() {
		// Sets up the socket
		try {
			socket = new DatagramSocket(Controls.UDP_PORT);
			socket.setSoTimeout(LISTEN_TO);
		} catch (SocketException se) {
			// Something has gone awefully wrong
			Controls.getLogger().severe("Cannot bind to " +
					"socket on port: " + Integer.valueOf(Controls.UDP_PORT).
					toString() + " (" + se.getMessage() + ")");
		}
		// Start running
		run = true;
		start();
		Controls.getLogger().fine("Started UDP transport");
	}
	
	/** The running code for the UDP transport.
	 */
	public void run() {
		run: {
			try {
				shutdownsem.acquire();
			} catch (InterruptedException ie) {
				Controls.getLogger().severe("Shutdown " +
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
							} else if (SearchIn.isType(packet.getData())) {
								type = "Search";
								inpack = new SearchIn(packet);
							} else if (ResultIn.isType(packet.getData())) {
								type = "Result";
								inpack = new ResultIn(packet);
							} else if (RequestIn.isType(packet.getData())) {
								type = "Request";
								inpack = new RequestIn(packet);
							} else  if (ResponseIn.isType(packet.getData())) {
								type = "Response";
								inpack = new ResponseIn(packet);
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
								Controls.getLogger().fine("Send packet to " + 
										send.getAddress().getHostAddress() + 
										" on " + Integer.valueOf(send.
										getPort()).toString());
							} catch (InterruptedException ie) {
								// This isn't going to happen
							} catch (IOException ie) {
								Controls.getLogger().severe("Unable to send " 
										+ "packet to: " + send.getAddress().
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
	
	/** Neatly closes the socket. After this call, the UDP transport should be
	 * stopped.
	 */
	public void close() {
		// Stop thread
		run = false;
		try {
			shutdownsem.acquire();
		} catch (InterruptedException ie) {
			Controls.getLogger().severe("Shutdown " +
					"interrupted!");
		}
		// Close socket
		if (socket != null) {
			socket.close();
		}
		Controls.getLogger().fine("Stopped UDP transport");
	}
	
	/** Enqueues a packet for sending.
	 * @param p The packet to be sent.
	 */
	public void send(PacketOut p) {
		try {
			sendlist.put(p.getPacket());
		} catch (InterruptedException ie) {
			Controls.getLogger().fine("Sendlist put " +
					"timeout! (" + ie.getMessage() + ")");
		}
	}
}
