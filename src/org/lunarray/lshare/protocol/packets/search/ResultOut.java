package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;

public class ResultOut extends PacketOut {

	private InetAddress to;
	
	public ResultOut(InetAddress t, SharedFile f) {
		to = t;
	}
	
	@Override
	public InetAddress getTarget() {
		return to;
	}
}
