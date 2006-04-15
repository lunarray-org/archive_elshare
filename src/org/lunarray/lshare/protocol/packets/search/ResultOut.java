package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

/**
 * An outgoing result.<br>
 * Packet 4:<br>
 * Purpose:<br>
 * Search for a filename.<br>
 * UDP broadcast.<br>
 * 3 fields:<br>
 * 0: 1 byte 0x11<br>
 * 1: 1 byte, representing an int n<br>
 * 2: n bytes, search query encoded in UTF-8
 * @author Pal Hargitai
 */
public class ResultOut extends PacketOut {

	/**
	 * The address to which the result is directed.
	 */
	private InetAddress to;
	
	/**
	 * Constructs an outgoing result.
	 * @param t The address to which the result is directed.
	 * @param f The entry to send.
	 */
	public ResultOut(InetAddress t, ShareEntry f) {
		to = t;

		// Construct data to send
		byte[] pathbytes = PacketUtil.encode(f.getPath());
		byte[] namebytes = PacketUtil.encode(f.getName());
		byte[] nlen = {Integer.valueOf(Math.min(namebytes.length, 255)).
				byteValue()};
		
		data = new byte[1 + 8 + 8 + Hash.length() + 2 + 1 + pathbytes.length + 
		                nlen[0]];
		data[0] = ResultIn.getType();
		
		// Enter the data
		if (f.isFile()) {
			PacketUtil.longToByteArray(f.getLastModified(), data, 1);
			PacketUtil.longToByteArray(f.getSize(), data, 1 + 8);
			PacketUtil.injectByteArrayIntoByteArray(f.getHash().getBytes(), 
					Hash.length(), 	data, 1 + 16);
		} else {
			
		}
		PacketUtil.shortUToByteArray(pathbytes.length, data, Hash.length() + 
				1 + 16);
		PacketUtil.injectByteArrayIntoByteArray(pathbytes, pathbytes.length,
				data, Hash.length() + 1 + 16 + 2);
		PacketUtil.injectByteArrayIntoByteArray(nlen, 1, data, Hash.length() + 
				1 + 16 + 2 + pathbytes.length);
		PacketUtil.injectByteArrayIntoByteArray(namebytes, nlen[0], data,
				Hash.length() + 1 + 16 + 2 + pathbytes.length + 1);
	}
	
	@Override
	/**
	 * Get the address to where the data is directed.
	 * @return The address to send the result to.
	 */
	public InetAddress getTarget() {
		return to;
	}
}
