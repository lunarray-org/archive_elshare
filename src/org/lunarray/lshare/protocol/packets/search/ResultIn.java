package org.lunarray.lshare.protocol.packets.search;

import java.net.DatagramPacket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.state.search.ResultHandler;
import org.lunarray.lshare.protocol.state.search.SearchResult;

/**
 * An incoming result.<br>
 * Packet 4:<br>
 * Purpose:<br>
 * Search for a filename.<br>
 * UDP broadcast.<br>
 * 3 fields:<br>
 * 0: 1 byte 0x11<br>
 * 1: 1 byte, representing an int n<br>
 * 2: n bytes, search query encoded in UTF-8<br>
 * @author Pal Hargitai
 */
public class ResultIn extends PacketIn {
    /**
     * The recieved packet.
     */
    private DatagramPacket packet;

    /**
     * The constructed search result.
     */
    private SearchResult result;

    /**
     * Constructs an incoming result.
     * @param p The datagram packet in which the search results resides.
     */
    public ResultIn(DatagramPacket p) {
        packet = p;
    }

    /**
     * Get the type of the packet.
     * @return The type of the packet.
     */
    public static byte getType() {
        return (byte) 0x10;
    }

    /**
     * Asks wether the given data is of a given result type.
     * @param data The data to check on if it's of a result type.
     * @return True if the given data is a result. False if not.
     */
    public static boolean isType(byte[] data) {
        return data[0] == getType();
    }

    @Override
    /**
     * Parse the result data and construct a result.
     * @throws MalformedPacketException Thrown if the packet is not readable.
     */
    public void parse() throws MalformedPacketException {
        try {
            if (getByte() != getType()) {
                throw new MalformedPacketException();
            }
            long ad = getLong();
            long size = getLong();
            Hash hash = getHash();
            String path = getLongString();
            String name = getShortString();

            result = new SearchResult(packet.getAddress(), path, name, hash,
                    ad, size);
        } catch (Exception e) {
            throw new MalformedPacketException();
        }
    }

    /**
     * Enqueues the result to be parsed elsewhere.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        c.getTasks().backgroundTask(new ResultHandler(result));
    }
}
