package org.lunarray.lshare.protocol.packets.download;

import java.net.DatagramPacket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.state.upload.UploadRequest;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * An incoming request for a file transfer.<br>
 * Packet 6:<br>
 * Purpose:<br>
 * Request for file transfer.<br>
 * UDP unicast.<br>
 * 8 fields:<br>
 * 0: 1 byte: 0x20<br>
 * 1: 8 bytes, the file offset<br>
 * 2: 8 bytes, the total size of the file<br>
 * 3: 16 bytes, the hash of the requested file<br>
 * 4: 2 bytes, representing an int n<br>
 * 5: n bytes, file path encoded in UTF-8<br>
 * 6: 1 byte, representing an int m<br>
 * 7: m bytes, file name encoded in UTF-8<br>
 * Timeout:<br>
 * Reponse is a packet type 7,8 or 9. Wait a second to retransmit, attempt at
 * most 3 times.
 * @author Pal Hargitai
 */
public class RequestIn extends PacketIn {
    /**
     * The recieved packet.
     */
    private DatagramPacket packet;

    /**
     * The download requeste represented in the packet.
     */
    private UploadRequest request;

    /**
     * Constructs an incoming result.
     * @param p The datagram packet in which the search results resides.
     */
    public RequestIn(DatagramPacket p) {
        packet = p;
    }

    /**
     * Get the type of the packet.
     * @return The type of the packet.
     */
    public static byte getType() {
        return (byte) 0x20;
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
     * Parse this packet
     * @throws MalformedPacketException Thrown if the packet is of an invalid
     * format.
     */
    public void parse() throws MalformedPacketException {
        try {
            if (getByte() != getType()) {
                throw new MalformedPacketException();
            }
            long offset = getLong();
            long size = getLong();
            Hash hash = getHash();
            String path = getLongString();
            String name = getShortString();

            request = new UploadRequest(path, name, hash, size,
                    offset);
        } catch (Exception e) {
            throw new MalformedPacketException();
        }
    }

    /**
     * Handles the request.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        User u = c.getState().getUserList().findUserByAddress(
                packet.getAddress());
        if (u == null) {
            Controls.getLogger().fine(
                    "Unauthorized user ("
                            + packet.getAddress().getHostAddress()
                            + ") requested file: " + request.getPath() + " "
                            + request.getName());
        }
        c.getState().getUploadManager().processRequest(u, request);
    }
}
