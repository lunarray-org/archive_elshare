package org.lunarray.lshare.protocol.packets.user;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.PacketUtil;

/** An incoming signon packet.<br>
 * Purpose:<br>
 * Signon packet.<br>
 * UDP broadcast.<br>
 * 5 fields:<br>
 * 0: 1 byte 0x00<br>
 * 1: 1 byte representing an int n<br>
 * 2: n bytes, user name encoded in UTF-8<br>
 * 3: 1 byte representing an int m<br>
 * 4: m bytes, challenge string encoded in UTF-8 (unique email address)<br>
 * Timeout:<br>
 * This packet should be broadcast every 20 seconds. If a packet has not been
 * received from a user in 1 minute, they are concered offline.
 * @author Pal Hargitai
 */
public class SignOnIn extends PacketIn {
	/** The packet that is to be handled.
	 */
	private DatagramPacket packet;
	
	/** The source of the packet.
	 */
	private InetAddress source;
	
	/** The username from the packet.
	 */
	private String username;
	
	/** The challenge from the packet.
	 */
	private String challenge;
	
	/** Gets the type of this packet.
	 * @return The packet identifier.
	 */
	public static byte getType() {
		return (byte)0x00;
	}
	
	/** Checks if the given data of this type.
	 * @param data The data to check.
	 * @return True if the data is of the given type. False if not.
	 */
	public static boolean isType(byte[] data) {
		return data[0] == getType();
	}
	
	/** Constructs an incoming signon packet.
	 * @param p The packet to constructs this data from.
	 */
	public SignOnIn(DatagramPacket p) {
		packet = p;
	}
	
	@Override
	/** Parses the packet.
	 * @throws MalformedPacketException Thrown if the packet data is corrupt.
	 */
	public void parse() throws MalformedPacketException {
		try {
			byte[] data = packet.getData();
			source = packet.getAddress();
			
			int unlen = data[1];
			byte[] unb = PacketUtil.getByteArrayFromByteArray(data, unlen, 2);
			String uns = PacketUtil.decode(unb);
			username = uns.trim();
			
			int uclen = data[unlen + 2];
			byte[] ucb = PacketUtil.getByteArrayFromByteArray(data, uclen, 
					unlen + 3);
			String ucs = PacketUtil.decode(ucb);
			challenge = ucs.trim();

			Controls.getLogger().finer("SignOn from: " + source.getHostName() +
					" (" + source.getHostAddress() + "); Name: " + username + 
					" Challenge: " + challenge);
		} catch (Exception e) {
			throw new MalformedPacketException();
		}
	}

	/** Handles the packet.
	 * @param c The controls to the rest of the protocol.
	 */
	public void runTask(Controls c) {
		c.getState().getUserList().signonUser(challenge, source, username);
	}
}
