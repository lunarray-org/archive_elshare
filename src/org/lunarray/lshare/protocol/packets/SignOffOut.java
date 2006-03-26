package org.lunarray.lshare.protocol.packets;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SignOffOut extends PacketOut {

	public SignOffOut() {
		
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
