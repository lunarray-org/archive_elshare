package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;

public class SearchOut extends PacketOut {
	
	public SearchOut(String q) {
		byte[] query = PacketUtil.encode(q);
		int qlen = Math.min(query.length, 255);
		
		int len = 1 + 1 + qlen;
		
		data = new byte[len];
		data[0] = SearchIn.getType();
		
		data[1] = Integer.valueOf(qlen).byteValue();
		PacketUtil.injectByteArrayIntoByteArray(query, qlen, data, 2);
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
