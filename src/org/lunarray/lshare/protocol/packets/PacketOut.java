package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

public abstract class PacketOut {

	public static String BROADCAST = "255.255.255.255";
	
	protected byte[] data;
	
	public byte[] getData() {
		return data;
	}
	
	public abstract InetAddress getTarget();
	
	public DatagramPacket getPacket() {
		DatagramPacket p = new DatagramPacket(getData(), getData().length, 
				getTarget(), Controls.UDP_PORT);
		return p;
	}
}
