package org.lunarray.lshare.protocol.packets.search;

import java.net.DatagramPacket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.Util;
import org.lunarray.lshare.protocol.state.search.ResultHandler;
import org.lunarray.lshare.protocol.state.search.SearchResult;

public class ResultIn extends PacketIn {

	private DatagramPacket packet;
	private SearchResult result;
	
	public ResultIn(DatagramPacket p) {
		packet = p;
	}
	
	public static byte getType() {
		return (byte)0x10;
	}
	
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	@Override
	public void parse() throws MalformedPacketException {
		try {
			byte[] data = packet.getData();
			
			long ad = Util.byteArrayToLong(data, 1);
			long size = Util.byteArrayToLong(data, 1 + 8);
			byte[] hash = Util.getByteArrayFromByteArray(data, HLEN, 1 + 8 + 8);
			
			short psize = Util.byteArrayToShortU(data, HLEN + 1 + 8 + 8);
			byte[] pbytes = Util.getByteArrayFromByteArray(data, psize, HLEN + 1 + 8 + 8 + 2);
			String path = Util.decode(pbytes);
			
			short nsize = data[HLEN + 1 + 8 + 8 + 2 + psize];
			byte[] nbytes = Util.getByteArrayFromByteArray(data, nsize, HLEN + 1 + 8 + 8 + 2 + psize + 1);
			String name = Util.decode(nbytes);
			
			result = new SearchResult(packet.getAddress(), path, name, hash, ad, size);
		} catch (Exception e) {
			throw new MalformedPacketException();
		}
	}

	public void runTask(Controls c) {
		c.getTasks().enqueueLazyTask(new ResultHandler(result));
	}

}
