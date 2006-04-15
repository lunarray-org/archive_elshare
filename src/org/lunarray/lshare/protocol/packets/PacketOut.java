package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

/**
 * An outbound packet.
 * @author Pal Hargitai
 */
public abstract class PacketOut {

	/**
	 * The address used for broadcasting packets.
	 */
	public final static String BROADCAST = "255.255.255.255";
	
	/**
	 * The data of the packet.
	 */
	protected byte[] data;
	
	/**
	 * Get the address that this packet is designated for. 
	 * @return The address that the packet is for.
	 */
	public abstract InetAddress getTarget();
	
	/**
	 * Get the data of this packet.
	 * @return The data.
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Get the packet that is to be sent.
	 * @return The packet.
	 */
	public DatagramPacket getPacket() {
		DatagramPacket p = new DatagramPacket(getData(), getData().length, 
				getTarget(), Controls.UDP_PORT);
		return p;
	}
}
