package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.tasks.RunnableTask;

public abstract class PacketIn implements RunnableTask {
	
	public static int HLEN = ShareSettings.HASH_UNSET.length;
	
	public abstract void parse() throws MalformedPacketException;
}
