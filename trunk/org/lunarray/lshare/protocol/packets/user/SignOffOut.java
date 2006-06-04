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

import org.lunarray.lshare.protocol.packets.PacketOut;

/**
 * An outgoing signoff packet.<br>
 * Packet 2:<br>
 * Purpose:<br>
 * Signoff packet.<br>
 * UDP broadcast.<br>
 * 1 field:<br>
 * 0: 1 byte 0x01
 * @author Pal Hargitai
 */
public class SignOffOut extends PacketOut {
    /**
     * Constructs a signoff packet.
     */
    public SignOffOut() {
        putByte(SignOffIn.getType());
    }

    @Override
    /**
     * Gets the packet target.
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
