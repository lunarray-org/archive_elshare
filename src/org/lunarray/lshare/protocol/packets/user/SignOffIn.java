package org.lunarray.lshare.protocol.packets.user;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketIn;

/**
 * An incoming signoff packet.<br>
 * Packet 2:<br>
 * Purpose:<br>
 * Signoff packet.<br>
 * UDP broadcast.<br>
 * 1 field:<br>
 * 0: 1 byte 0x01
 * @author Pal Hargitai
 */
public class SignOffIn extends PacketIn {
	
	/**
	 * The packet that has been recieved.
	 */
	private DatagramPacket packet;
	
	/**
	 * The address from where the address originated.
	 */
	private InetAddress address;
	
	/**
	 * Gets the packet type identfier.
	 * @return The packet identifier.
	 */
	public static byte getType() {
		return (byte)0x01;
	}
	
	/**
	 * Checks wether the given data is of this type.
	 * @param data The data to check.
	 * @return True if the given packet is of this type, false if not.
	 */
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}

	/**
	 * Constructs a signoff packet.
	 * @param p The packet in which the signoff resides.
	 */
	public SignOffIn(DatagramPacket p) {
		packet = p;
	}

	@Override
	/**
	 * Parsing the packet.
	 */
	public void parse() {
		address = packet.getAddress(); 
	}

	/**
	 * Handling the signoff task.
	 * @param c The controls for the protocol.
	 */
	public void runTask(Controls c) {
		c.getState().getUserList().signoffUser(address);
	}
}
