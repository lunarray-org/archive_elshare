package org.lunarray.lshare.protocol.packets.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.packets.PacketOut;

/**
 * An outgoing signoff packet.<br>
 * Packet 2:<br>
 * Purpose:<br>
 * Signoff packet.<br>
 * UDP broadcast.<br>
 * 1 field:<br>
 * 0: 1 byte 0x01
 * @author Pal Hargitai
 */
public class SignOffOut extends PacketOut {

	/**
	 * Constructs a signoff packet.
	 */
	public SignOffOut() {
		data = new byte[1];
		data[0] = SignOffIn.getType();
	}
	
	@Override
	/**
	 * Gets the packet target.
	 * @return The address to which this packet is targeted.
	 */
	public InetAddress getTarget() {
		try {
			return InetAddress.getByName(BROADCAST);
		} catch (UnknownHostException uhe) {
			return null;			
		}
	}
}
