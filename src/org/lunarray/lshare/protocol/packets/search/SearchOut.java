package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.packets.PacketOut;

/**
 * An outgoing search request.<br>
 * Packet 4:<br>
 * Purpose:<br>
 * Search for a filename.<br>
 * UDP broadcast.<br>
 * 3 fields:<br>
 * 0: 1 byte 0x11<br>
 * 1: 1 byte, representing an int n<br>
 * 2: n bytes, search query encoded in UTF-8
 * @author Pal Hargitai
 */
public class SearchOut extends PacketOut {
    /**
     * Constructs an outgoing search request.
     * @param q The query to search for.
     */
    public SearchOut(String q) {
        putByte(SearchIn.getType());
        putShortString(q);
    }

    @Override
    /**
     * Gets the target of the packet.
     * @return The address where to packet is designated for.
     */
    public InetAddress getTarget() {
        try {
            return InetAddress.getByName(BROADCAST);
        } catch (UnknownHostException uhe) {
            return null;
        }
    }
}
