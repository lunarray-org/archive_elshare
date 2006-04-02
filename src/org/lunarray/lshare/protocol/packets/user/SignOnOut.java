package org.lunarray.lshare.protocol.packets.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.Util;

/*
 * Purpose:
 * Signon packet.
 * UDP broadcast.
 * 4 fields:
 * 0: 1 byte 0x00
 * 1: 1 byte representing an int n
 * 2: n bytes, user name encoded in UTF-8
 * 3: 20 bytes, SHA-1 challenge password of the user
 * Timeout:
 * This packet should be broadcast every 20 seconds. If a packet has not been
 * received from a user in 1 minute, they are concered offline.
 */
public class SignOnOut extends PacketOut {
	
	public SignOnOut(Controls c) {
		String username = c.getSettings().getUsername();
		byte[] un = Util.encode(username);
		int unlen = Math.min(un.length, 255);

		String challenge = c.getSettings().getChallenge();
		byte[] uc = Util.encode(challenge);
		int uclen = Math.min(uc.length, 255);

		int len = 1 + 1 + unlen + 1 + uclen;
		
		data = new byte[len];
		data[0] = SignOnIn.getType();
		
		data[1] = Integer.valueOf(unlen).byteValue();
		Util.injectByteArrayIntoByteArray(un, unlen, data, 2);
		
		data[2 + unlen] = Integer.valueOf(uclen).byteValue();
		Util.injectByteArrayIntoByteArray(uc, uclen, data, 3 + unlen);
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
