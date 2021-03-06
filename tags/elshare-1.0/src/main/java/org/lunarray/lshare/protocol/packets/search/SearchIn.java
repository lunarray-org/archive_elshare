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

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.MalformedPacketException;
import org.lunarray.lshare.protocol.packets.PacketIn;
import org.lunarray.lshare.protocol.state.search.StringSearchHandler;

/**
 * An incoming search request.<br>
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
public class SearchIn extends PacketIn {
    /**
     * The incoming search request.
     */
    private String query;

    /**
     * The datagram packet that has been received.
     */
    private DatagramPacket packet;

    /**
     * The source of the search.
     */
    private InetAddress source;

    /**
     * Constructs an incoming search request.
     * @param p The packet that came in.
     */
    public SearchIn(DatagramPacket p) {
        packet = p;
    }

    /**
     * Gets the type of the packet.
     * @return The type of the packet.
     */
    public static byte getType() {
        return (byte) 0x11;
    }

    /**
     * Checks if the data is of this type.
     * @param data The data to check the type of.
     * @return True if the given data is of this type, false if not.
     */
    public static boolean isType(byte[] data) {
        return data[0] == getType();
    }

    @Override
    /**
     * Parse the current packet.
     * @throws MalformedPacketException Thrown if the packet is not readable.
     */
    public void parse() throws MalformedPacketException {
        source = packet.getAddress();
        try {
            if (getByte() != getType()) {
                throw new MalformedPacketException();
            }
            query = getShortString();
            
        } catch (Exception e) {
            throw new MalformedPacketException();
        }
    }

    /**
     * Runs the task of parsing the packet.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        Controls.getLogger().finer("Search for: " + query);
        Controls.getLogger().finer("From: " + source.getHostName());
        c.getTasks().backgroundTask(new StringSearchHandler(source, query));
    }
}
