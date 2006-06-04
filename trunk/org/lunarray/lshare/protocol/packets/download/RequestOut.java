/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.protocol.packets.download;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * An outgoing request for a file transfer.<br>
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
public class RequestOut extends PacketOut {
    /**
     * The user this request is meant for.
     */
    private User user;

    /**
     * Constructs an outgoing request for a filetransfer.
     * @param u The user it is meant for.
     * @param f The remote entry requested.
     * @param offset The offset of the entry where downloading should commence.
     * @throws UserNotFound Thrown if a user isn't logged in.
     */
    public RequestOut(User u, RemoteFile f, long offset) throws UserNotFound {
        if (u.getAddress() == null) {
            throw new UserNotFound();
        }
        user = u;

        putByte(RequestIn.getType());
        putLong(offset);
        putLong(f.getSize());
        putHash(f.getHash());
        putLongString(f.getPath());
        putShortString(f.getName());
    }

    @Override
    /**
     * Gets the target of the packet.
     * @return The target of the packet.
     */
    public InetAddress getTarget() {
        return user.getAddress();
    }
}