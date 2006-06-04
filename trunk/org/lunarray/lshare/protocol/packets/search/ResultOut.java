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
