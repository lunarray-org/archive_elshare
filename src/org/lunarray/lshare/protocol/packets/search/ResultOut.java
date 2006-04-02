package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.Util;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;

public class ResultOut extends PacketOut {

	private InetAddress to;
	
	public ResultOut(InetAddress t, SharedFile f) {
		to = t;

		byte[] pathbytes = Util.encode(f.getPath());
		byte[] namebytes = Util.encode(f.getName());
		byte[] nlen = {Integer.valueOf(Math.min(namebytes.length, 255)).byteValue()};
		
		data = new byte[1 + 8 + 8 + HLEN + 2 + 1 + pathbytes.length + nlen[0]];
		
		data[0] = ResultIn.getType();
		
		Util.longToByteArray(f.getLastModified(), data, 1);
		Util.longToByteArray(f.getSize(), data, 1 + 8);
		Util.injectByteArrayIntoByteArray(f.getHash(), HLEN, data, 1 + 16);
		Util.shortUToByteArray(pathbytes.length, data, HLEN + 1 + 16);
		Util.injectByteArrayIntoByteArray(pathbytes, pathbytes.length, data, HLEN + 1 + 16 + 2);
		Util.injectByteArrayIntoByteArray(nlen, 1, data, HLEN + 1 + 16 + 2 + pathbytes.length);
		Util.injectByteArrayIntoByteArray(namebytes, nlen[0], data, HLEN + 1 + 16 + 2 + pathbytes.length + 1);
	}
	
	@Override
	public InetAddress getTarget() {
		return to;
	}
}
