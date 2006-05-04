package org.lunarray.lshare.protocol.packets.download;

import java.net.DatagramPacket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.download.FileResponse;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * An incoming response for a request for a file transfer.<br>
 * Packet 7:<br>
 * Purpose:<br>
 * Response to file transfer, transfer details<br>
 * UDP unicast.<br>
 * 9 fields:<br>
 * 0: 1 byte: 0x21<br>
 * 1: 8 bytes, the offset at which sending is started<br>
 * 2: 8 bytes, the total size of the file<br>
 * 3: 2 bytes, the port number of the port that is to be sent on<br>
 * 4: 16 bytes, the hash of the requested file<br>
 * 5: 2 bytes, representing an int n<br>
 * 6: n bytes, file path encoded in UTF-8<br>
 * 7: 1 byte, representing an int m<br>
 * 8: m bytes, file name encoded in UTF-8
 * @author Pal Hargitai
 */
public class ResponseIn extends PacketIn {
    /**
     * The recieved packet.
     */
    private DatagramPacket packet;

    /**
     * The response represented in this packet.
     */
    private FileResponse response;

    /**
     * Constructs an incoming result.
     * @param p The datagram packet in which the search results resides.
     */
    public ResponseIn(DatagramPacket p) {
        packet = p;
    }

    /**
     * Get the type of the packet.
     * @return The type of the packet.
     */
    public static byte getType() {
        return (byte) 0x21;
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
     * Parses the packet.
     * @throws MalformedPacketException Thrown if packet is of an invalid
     * format.
     */
    public void parse() throws MalformedPacketException {
        try {
            byte[] data = packet.getData();

            long offset = PacketUtil.byteArrayToLong(data, 1);

            long size = PacketUtil.byteArrayToLong(data, 9);

            int port = PacketUtil.byteArrayToShortU(data, 17);

            byte[] hash = PacketUtil.getByteArrayFromByteArray(data, Hash
                    .length(), 19);

            short psize = PacketUtil
                    .byteArrayToShortU(data, Hash.length() + 19);
            byte[] pbytes = PacketUtil.getByteArrayFromByteArray(data, psize,
                    Hash.length() + 21);
            String path = PacketUtil.decode(pbytes).trim();

            short nsize = data[Hash.length() + 21 + psize];
            byte[] nbytes = PacketUtil.getByteArrayFromByteArray(data, nsize,
                    Hash.length() + 22 + psize);
            String name = PacketUtil.decode(nbytes).trim();
            response = new FileResponse(path, name, new Hash(hash), size,
                    offset, port);
        } catch (Exception e) {
            throw new MalformedPacketException();
        }
    }

    /**
     * Handles the response.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        try {
            c.getState().getDownloadManager().handleResponse(
                    response,
                    c.getState().getUserList().findUserByAddress(
                            packet.getAddress()));
        } catch (UserNotFound unf) {
            // Ignore
        }
    }
}
