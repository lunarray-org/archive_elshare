package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * An inbound packet.
 * @author Pal Hargitai
 */
public abstract class PacketIn implements RunnableTask {
	
	/**
	 * The length of a packet.
	 */
	public static int HLEN = ShareSettings.HASH_UNSET.length;

	/**
	 * Parses a packet.
	 * @throws MalformedPacketException Thrown if the packet is malformed.
	 */
	public abstract void parse() throws MalformedPacketException;
}
