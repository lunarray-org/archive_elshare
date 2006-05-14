package org.lunarray.lshare.protocol.packets.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

/**
 * An outgoing result.<br>
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
public class ResultOut extends PacketOut {
    /**
     * The address to which the result is directed.
     */
    private InetAddress to;

    /**
     * Constructs an outgoing result.
     * @param t The address to which the result is directed.
     * @param f The entry to send.
     */
    public ResultOut(InetAddress t, ShareEntry f) {
        to = t;

        putByte(ResultIn.getType());
        putLong(f.getLastModified());
        putLong(f.getSize());
        putHash(f.getHash());
        putLongString(f.getPath());
        putShortString(f.getName());
    }

    @Override
    /**
     * Get the address to where the data is directed.
     * @return The address to send the result to.
     */
    public InetAddress getTarget() {
        return to;
    }
}
