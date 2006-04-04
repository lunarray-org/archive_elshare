package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.protocol.Controls;

/**
 * An invalid packet that does nothing.
 * @author Pal Hargitai
 */
public class InvalidPacket extends PacketIn {
	
	@Override
	/**
	 * Parse the packet.
	 */
	public void parse() {
		// Do nothing here.
	}

	/**
	 * Run the task.
	 * @param c The controls for the protocol.
	 */
	public void runTask(Controls c) {
		// Ignore
	}
}
