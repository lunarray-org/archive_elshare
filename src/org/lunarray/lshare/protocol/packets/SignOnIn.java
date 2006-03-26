package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

public class SignOnIn extends PacketIn {
	
	private DatagramPacket packet;
	private InetAddress source;
	private String username;
	private String challenge;
	
	public static byte getType() {
		return (byte)0x00;
	}
	
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	public SignOnIn(DatagramPacket p) {
		packet = p;
	}
	
	@Override
	public void parse() {
		byte[] data = packet.getData();
		source = packet.getAddress();
		
		int unlen = data[1];
		byte[] unb = Util.getByteArrayFromByteArray(data, unlen, 2);
		String uns = Util.decode(unb);
		username = uns;
		
		int uclen = data[unlen + 2];
		byte[] ucb = Util.getByteArrayFromByteArray(data, uclen, unlen + 3);
		String ucs = Util.decode(ucb);
		challenge = ucs;

		Controls.getLogger().finer("SignOn from: " + source.getHostName() +
				" (" + source.getHostAddress() + "); Name: " + username + 
				" Challenge: " + challenge);
	}

	public void runTask(Controls c) {
		c.getState().getUserList().signonUser(challenge, source, username);
	}
}
