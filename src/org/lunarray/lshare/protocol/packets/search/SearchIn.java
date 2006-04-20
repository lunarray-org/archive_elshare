package org.lunarray.lshare.protocol.packets.search;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.search.StringSearchHandler;

/** An incoming search request.<br>
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
public class SearchIn extends PacketIn {
	/** The incoming search request.
	 */
	private String query;
	
	/** The datagram packet that has been received.
	 */
	private DatagramPacket packet;
	
	/** The source of the search.
	 */
	private InetAddress source;
	
	/** Constructs an incoming search request.
	 * @param p The packet that came in.
	 */
	public SearchIn(DatagramPacket p) {
		packet = p;
	}
	
	/** Gets the type of the packet.
	 * @return The type of the packet.
	 */
	public static byte getType() {
		return (byte)0x11;
	}
	
	/** Checks if the data is of this type.
	 * @param data The data to check the type of.
	 * @return True if the given data is of this type, false if not.
	 */
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	@Override
	/** Parse the current packet.
	 * @throws MalformedPacketException Thrown if the packet is not readable.
	 */
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

	/** Runs the task of parsing the packet.
	 * @param c The controls to the protocol.
	 */
	public void runTask(Controls c) {
		Controls.getLogger().finer("Search for: " + query);
		Controls.getLogger().finer("From: " + source.getHostName());
		c.getTasks().backgroundTask(new StringSearchHandler(source, query));
	}
}
