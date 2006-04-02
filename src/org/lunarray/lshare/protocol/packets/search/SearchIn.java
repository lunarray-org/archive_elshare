package org.lunarray.lshare.protocol.packets.search;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.search.StringSearchHandler;

public class SearchIn extends PacketIn {
	
	private String query;
	private DatagramPacket packet;
	private InetAddress source;
	
	public SearchIn(DatagramPacket p) {
		packet = p;
	}
	
	public static byte getType() {
		return (byte)0x11;
	}
	
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	@Override
	public void parse() throws MalformedPacketException {
		source = packet.getAddress();
		try {
			byte[] data = packet.getData();
			
			int qlen = data[1];
			byte[] qb = PacketUtil.getByteArrayFromByteArray(data, qlen, 2);
			query = PacketUtil.decode(qb).trim();
		} catch (Exception e) {
			throw new MalformedPacketException();
		}
	}

	public void runTask(Controls c) {
		Controls.getLogger().finer("Search for: " + query);
		Controls.getLogger().finer("From: " + source.getHostName());
		c.getTasks().backgroundTask(new StringSearchHandler(source, query));
	}
}
