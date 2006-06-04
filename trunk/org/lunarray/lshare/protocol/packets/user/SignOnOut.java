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
package org.lunarray.lshare.protocol.packets.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketOut;

/**
 * An outgoing signon packet.<br>
 * Purpose:<br>
 * Signon packet.<br>
 * UDP broadcast.<br>
 * 5 fields:<br>
 * 0: 1 byte 0x00<br>
 * 1: 1 byte representing an int n<br>
 * 2: n bytes, user name encoded in UTF-8<br>
 * 3: 1 byte representing an int m<br>
 * 4: m bytes, challenge string encoded in UTF-8 (unique email address)<br>
 * Timeout:<br>
 * This packet should be broadcast every 20 seconds. If a packet has not been
 * received from a user in 1 minute, they are concered offline.
 * @author Pal Hargitai
 */
public class SignOnOut extends PacketOut {
    /**
     * Constructs an outgoing sign on packet.
     * @param c The controls to the rest of the protocol.
     */
    public SignOnOut(Controls c) {
        putByte(SignOnIn.getType());
        putShortString(c.getSettings().getUsername());
        putShortString(c.getSettings().getChallenge());
    }

    @Override
    /**
     * Gets the target to which this packet is intended.
     * @return The address to which this packet is targeted.
     */
    public InetAddress getTarget() {
        try {
            return InetAddress.getByName(BROADCAST);
        } catch (UnknownHostException uhe) {
            return null;
        }
    }
}
