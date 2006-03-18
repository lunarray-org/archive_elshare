package edu.tue.compnet.protocol.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;

/**
 * Describes a packet of type 7
 * Packet seven looks as follows:<br>
 * (1)
 * 1: 1 byte that denotes the type, has the number 7<br>
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketSeven extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x07;
	/** The amount of timeouts that may occur. */
	public static int TIMEOUTS = 2;
	/** The amount of time between each timeout. */
	public static int TIMEOUT = 1000;

	/**
	 * The constructor of a packet of type seven.
	 * @param address The address to send on.
	 * @param port The port to send on.
	 * @param state The state of the protocol.
	 * @param transport The transport.
	 */
	public PacketSeven(InetAddress address, int port, State state,
			Transport transport) {
		byte[] data = new byte[1];
		data[0] = TYPE;
		packet = new DatagramPacket(data, data.length, address, port);
		
		state.getPacketTimeoutList().addPacketTimeout(new
				PacketSevenTimeout(TIMEOUTS, TIMEOUT, address, state,
				transport, this));
		state.getUserlistRequestList().put(address);
	}

	/**
	 * Handles when a packet of that kind has arrived.
	 * @param packet The packet that has arrived.
	 * @param state The state of the protocol.
	 * @param daemon The daemon, to queue packets to be sent back.
	 */
	public static void handlePacket(DatagramPacket packet,
			State state, Transport daemon) {
		/*
		 * First check wether it has really been a packet of type 7. Then
		 * send the file list
		 */
		byte[] data = packet.getData();
		// Something went wrong
		if (data[0] != TYPE) {
			return;
		}
		Output.out("Packet type seven received!");
		
		// send a packet of type 8
		PacketEight pe = new PacketEight(packet.getAddress(), Transport.PORT,
				Transport.MTU, state);
		daemon.send(pe.getPacket());
		Output.out("Packet type eight enqueued!");
	}
}