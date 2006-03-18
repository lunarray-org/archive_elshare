package edu.tue.compnet.protocol.packet;

import java.net.InetAddress;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;

/**
 * Handles the tiemouts of a packet of type 7
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketSevenTimeout extends PacketTimeout {
	// The address to check for
	InetAddress address;
	// The state where we will check
	State state;
	// The transport
	Transport transport;
	// The packet
	PacketSeven packet;

	/**
	 * The timeout for a packet of type 7
	 * @param a The amount of timeouts.
	 * @param t The time between tiemouts.
	 * @param ad The address to request from.
	 * @param s The state of the protocol.
	 * @param trans The transport.
	 * @param p The packet
	 */
	public PacketSevenTimeout(int a, int t, InetAddress ad, State s,
			Transport trans, PacketSeven p) {
		super(a, t);
		address = ad;
		state = s;
		packet = p;
		transport = trans;		
	}
	
	@Override
	public void lock() {
		// None here
	}

	@Override
	public void unlock() {
		// None here
	}

	@Override
	public boolean isValid() {
		return state.getUserlistRequestList().exists(address);
	}

	@Override
	public void handleTimeout() {
		transport.send(packet.getPacket());
		Output.out("Handle timeout type seven!");
	}

	@Override
	public void cleanUp() {
		state.getUserlistRequestList().take(address);
		Output.out("Cleaning up type seven!");
	}

}
