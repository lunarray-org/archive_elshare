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
package org.lunarray.lshare.protocol.packets;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * An inbound packet.
 * @author Pal Hargitai
 */
public abstract class PacketIn implements RunnableTask {

    /**
     * The current index where data is being read from.
     */
    private int index;

    /**
     * The current data that is being read from.
     */
    private byte[] data;

    /**
     * Sets the data to be read from.
     * @param d The (new) data to be read from.
     */
    public void setData(byte[] d) {
        index = 0;
        data = d;
    }

    /**
     * Gets a single byte.
     * @return A single byte at the current index.
     */
    protected byte getByte() {
        byte b = data[index];
        index++;
        return b;
    }

    /**
     * Gets a long.
     * @return A long at the current index.
     */
    protected long getLong() {
        long l = PacketUtil.byteArrayToLong(data, index);
        index += 8;
        return l;
    }

    /**
     * Gets a hash.
     * @return A hash at the current index.
     */
    protected Hash getHash() {
        byte[] h = PacketUtil.getByteArrayFromByteArray(data, Hash.length(),
                index);
        index += Hash.length();
        return new Hash(h);
    }
    
    /**
     * Gets an unsigned short.
     * @return An unsigned short at the current index.
     */
    protected int getShortU() {
        int i = PacketUtil.byteArrayToShortU(data, index);
        index += 2;
        return i;
    }
    
    /**
     * Gets a long string with a maximal length of 65536.
     * @return The string at the current index.
     */
    protected String getLongString() {
        short ssize = PacketUtil.byteArrayToShortU(data, index);
        index += 2;
        byte[] sbytes = PacketUtil.getByteArrayFromByteArray(data, ssize, index);
        index += ssize;
        return PacketUtil.decode(sbytes).trim();
    }
    
    /**
     * Gets a short string with a maximal length of 255.
     * @return The string at the current index.
     */
    protected String getShortString() {
        byte ssize = data[index];
        index += 1;
        byte[] sbytes = PacketUtil.getByteArrayFromByteArray(data, ssize, index);
        index += ssize;
        return PacketUtil.decode(sbytes).trim();        
    }

    /**
     * Parses a packet.
     * @throws MalformedPacketException Thrown if the packet is malformed.
     */
    public abstract void parse() throws MalformedPacketException;
}
