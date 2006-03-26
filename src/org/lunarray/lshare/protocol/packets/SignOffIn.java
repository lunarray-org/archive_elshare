package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

public class SignOffIn extends PacketIn {
	
	private DatagramPacket packet;
	private InetAddress address;
	
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
		address = packet.getAddress(); 
	}

	public void runTask(Controls c) {
		c.getState().getUserList().signoffUser(address);
	}
}
