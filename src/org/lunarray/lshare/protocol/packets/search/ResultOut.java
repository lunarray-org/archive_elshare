package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

public class ResultOut extends PacketOut {

	private InetAddress to;
	
	public ResultOut(InetAddress t, ShareEntry f) {
		to = t;

		byte[] pathbytes = PacketUtil.encode(f.getPath());
		byte[] namebytes = PacketUtil.encode(f.getName());
		byte[] nlen = {Integer.valueOf(Math.min(namebytes.length, 255)).byteValue()};
		
		data = new byte[1 + 8 + 8 + HLEN + 2 + 1 + pathbytes.length + nlen[0]];
		
		data[0] = ResultIn.getType();
		
		if (f.isFile()) {
			PacketUtil.longToByteArray(f.getLastModified(), data, 1);
			PacketUtil.longToByteArray(f.getSize(), data, 1 + 8);
			PacketUtil.injectByteArrayIntoByteArray(f.getHash(), HLEN, data, 1 + 16);
		} else {
			
		}
		PacketUtil.shortUToByteArray(pathbytes.length, data, HLEN + 1 + 16);
		PacketUtil.injectByteArrayIntoByteArray(pathbytes, pathbytes.length, data, HLEN + 1 + 16 + 2);
		PacketUtil.injectByteArrayIntoByteArray(nlen, 1, data, HLEN + 1 + 16 + 2 + pathbytes.length);
		PacketUtil.injectByteArrayIntoByteArray(namebytes, nlen[0], data, HLEN + 1 + 16 + 2 + pathbytes.length + 1);
	}
	
	@Override
	public InetAddress getTarget() {
		return to;
	}
}
