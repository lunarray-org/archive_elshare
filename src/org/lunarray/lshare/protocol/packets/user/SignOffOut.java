package org.lunarray.lshare.protocol.packets.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.packets.PacketOut;

/*
 * Packet 2:
 * Purpose:
 * Signoff packet.
 * UDP broadcast.
 * 1 field:
 * 0: 1 byte 0x01
 */

public class SignOffOut extends PacketOut {

	public SignOffOut() {
		data = new byte[1];
		data[0] = SignOffIn.getType();
	}
	
	@Override
	public InetAddress getTarget() {
		try {
			return InetAddress.getByName(BROADCAST);
		} catch (UnknownHostException uhe) {
			return null;			
		}
	}
}
