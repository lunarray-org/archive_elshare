package org.lunarray.lshare.protocol.packets.download;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * An outgoing response for a request for a file transfer.<br>
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
public class ResponseOut extends PacketOut {
    /**
     * The user that this packet is to be sent to.
     */
    private User user;

    /**
     * Constructs an outgoing response for a request for file transfer.
     * @param r The remote entry.
     * @param port The port at which the transfer may occur.
     * @param u The user it is to be sent to.
     * @param offset The offset of the file.
     * @throws UserNotFound Thrown if the user cannot be found.
     */
    public ResponseOut(RemoteFile r, int port, User u, long offset)
            throws UserNotFound {
        // Construct data
        if (u.getAddress() == null) {
            throw new UserNotFound();
        }
        user = u;

        putByte(ResponseIn.getType());
        putLong(offset);
        putLong(r.getSize());
        putShortU(port);
        putHash(r.getHash());
        putLongString(r.getPath());
        putShortString(r.getName());
    }

    @Override
    /**
     * Gets the target for the packet.
     * @return The target for the packet.
     */
    public InetAddress getTarget() {
        return user.getAddress();
    }
}
