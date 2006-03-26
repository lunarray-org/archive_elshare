package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.tasks.RunnableTask;

public abstract class PacketIn implements RunnableTask {
	
	public abstract void parse() throws MalformedPacketException;
}
