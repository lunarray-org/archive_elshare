package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;

import org.lunarray.lshare.protocol.Controls;

public class SignOffIn extends PacketIn {
	
	private DatagramPacket packet;
	
	public static byte getType() {
		return (byte)0x01;
	}
	
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}

	public SignOffIn(DatagramPacket p) {
		packet = p;
	}

	@Override
	public void parse() {
		packet.getData();
	}

	public void runTask(Controls c) {
		// TODO
	}
}
