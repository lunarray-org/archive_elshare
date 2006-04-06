package org.lunarray.lshare.protocol.packets.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;

/**
 * An outgoing signon packet.<br>
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
public class SignOnOut extends PacketOut {
	
	/**
	 * Constructs an outgoing sign on packet.
	 * @param c The controls to the rest of the protocol.
	 */
	public SignOnOut(Controls c) {
		String username = c.getSettings().getUsername();
		byte[] un = PacketUtil.encode(username);
		int unlen = Math.min(un.length, 255);

		String challenge = c.getSettings().getChallenge();
		byte[] uc = PacketUtil.encode(challenge);
		int uclen = Math.min(uc.length, 255);

		int len = 1 + 1 + unlen + 1 + uclen;
		
		data = new byte[len];
		data[0] = SignOnIn.getType();
		
		data[1] = Integer.valueOf(unlen).byteValue();
		PacketUtil.injectByteArrayIntoByteArray(un, unlen, data, 2);
		
		data[2 + unlen] = Integer.valueOf(uclen).byteValue();
		PacketUtil.injectByteArrayIntoByteArray(uc, uclen, data, 3 + unlen);
	}
	
	@Override
	/**
	 * Gets the target to which this packet is intended.
	 * @return The address to which this packet is targeted.
	 */
	public InetAddress getTarget() {
		try {
			return InetAddress.getByName(BROADCAST);
		} catch (UnknownHostException uhe) {
			return null;			
		}
	}
}
