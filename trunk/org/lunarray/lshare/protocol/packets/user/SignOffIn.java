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

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketIn;

/**
 * An incoming signoff packet.<br>
 * Packet 2:<br>
 * Purpose:<br>
 * Signoff packet.<br>
 * UDP broadcast.<br>
 * 1 field:<br>
 * 0: 1 byte 0x01
 * @author Pal Hargitai
 */
public class SignOffIn extends PacketIn {
    /**
     * The packet that has been recieved.
     */
    private DatagramPacket packet;

    /**
     * The address from where the address originated.
     */
    private InetAddress address;

    /**
     * Gets the packet type identfier.
     * @return The packet identifier.
     */
    public static byte getType() {
        return (byte) 0x01;
    }

    /**
     * Checks wether the given data is of this type.
     * @param data The data to check.
     * @return True if the given packet is of this type, false if not.
     */
    public static boolean isType(byte[] data) {
        return data[0] == getType();
    }

    /**
     * Constructs a signoff packet.
     * @param p The packet in which the signoff resides.
     */
    public SignOffIn(DatagramPacket p) {
        packet = p;
    }

    @Override
    /**
     * Parsing the packet.
     */
    public void parse() {
        address = packet.getAddress();
    }

    /**
     * Handling the signoff task.
     * @param c The controls for the protocol.
     */
    public void runTask(Controls c) {
        c.getState().getUserList().signoffUser(address);
    }
}
