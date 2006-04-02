package org.lunarray.lshare.protocol.packets.user;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.PacketUtil;

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
		byte[] unb = PacketUtil.getByteArrayFromByteArray(data, unlen, 2);
		String uns = PacketUtil.decode(unb);
		username = uns.trim();
		
		int uclen = data[unlen + 2];
		byte[] ucb = PacketUtil.getByteArrayFromByteArray(data, uclen, unlen + 3);
		String ucs = PacketUtil.decode(ucb);
		challenge = ucs.trim();

		Controls.getLogger().finer("SignOn from: " + source.getHostName() +
				" (" + source.getHostAddress() + "); Name: " + username + 
				" Challenge: " + challenge);
	}

	public void runTask(Controls c) {
		c.getState().getUserList().signonUser(challenge, source, username);
	}
}
