package org.lunarray.lshare.protocol.packets.download;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;

public class RequestIn extends PacketIn {

	/**
	 * Get the type of the packet.
	 * @return The type of the packet.
	 */
	public static byte getType() {
		return (byte)0x20;
	}
	
	@Override
	public void parse() throws MalformedPacketException {
		// TODO Auto-generated method stub
	}

	public void runTask(Controls c) {
		// TODO Auto-generated method stub
	}
}
