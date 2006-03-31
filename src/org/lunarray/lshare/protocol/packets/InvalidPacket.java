package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.protocol.Controls;

public class InvalidPacket extends PacketIn {
	@Override
	public void parse() {
		// Do nothing here.
	}

	public void runTask(Controls c) {
		// Ignore
	}
}
