package org.lunarray.lshare.protocol.packets.search;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;

public class ResultIn extends PacketIn {

	public static byte getType() {
		return (byte)0x10;
	}
	
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	@Override
	public void parse() throws MalformedPacketException {
		// TODO Auto-generated method stub
	}

	public void runTask(Controls c) {
		// TODO Auto-generated method stub
	}

}
