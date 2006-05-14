package org.lunarray.lshare.protocol.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;

/**
 * An outbound packet.
 * @author Pal Hargitai
 */
public abstract class PacketOut {
    /**
     * The address used for broadcasting packets.
     */
    public final static String BROADCAST = "255.255.255.255";

    /**
     * The data of the packet.
     */
    private byte[] data;

    /**
     * The current index where data is being written to.
     */
    private int index;

    /**
     * Constructs an outgoing packet.
     */
    public PacketOut() {
        data = new byte[Controls.UDP_MTU];
        index = 0;
    }

    /**
     * Puts a single byte.
     * @param b The byte to be written at the current index.
     */
    public void putByte(byte b) {
        data[index] = b;
        index++;
    }

    /**
     * Puts a long.
     * @param l The long to be written at the current index.
     */
    public void putLong(long l) {
        PacketUtil.longToByteArray(l, data, index);
        index += 8;
    }

    /**
     * Puts an unsigned short.
     * @param s The short to be written at the current index.
     */
    public void putShortU(int s) {
        PacketUtil.shortUToByteArray(s, data, index);
        index += 2;
    }

    /**
     * Puts a hash.
     * @param h The hash to be written at the current index.
     */
    public void putHash(Hash h) {
        PacketUtil.injectByteArrayIntoByteArray(h.getBytes(), Hash.length(),
                data, index);
        index += Hash.length();
    }

    /**
     * Puts a string with a maximum length of 65536.
     * @param s The string to be written at the current index.
     */
    public void putLongString(String s) {
        byte[] bs = PacketUtil.encode(s);

        PacketUtil.shortUToByteArray(bs.length, data, index);
        index += 2;
        PacketUtil.injectByteArrayIntoByteArray(bs, bs.length, data, index);
        index += bs.length;

    }

    /**
     * Puts a string with a maximum length of 255.
     * @param s The string to be written at the current index.
     */
    public void putShortString(String s) {
        byte[] bs = PacketUtil.encode(s);
        byte slen = (byte) Math.min(bs.length, 255);

        data[index] = slen;
        index++;
        PacketUtil.injectByteArrayIntoByteArray(bs, slen, data, index);
        index += slen;
    }

    /**
     * Get the address that this packet is designated for.
     * @return The address that the packet is for.
     */
    public abstract InetAddress getTarget();

    /**
     * Get the data of this packet.
     * @return The data put here.
     */
    public byte[] getData() {
        byte[] r = new byte[index];
        System.arraycopy(data, 0, r, 0, index);
        return r;
    }

    /**
     * Get the packet that is to be sent.
     * @return The packet.
     */
    public DatagramPacket getPacket() {
        DatagramPacket p = new DatagramPacket(getData(), getData().length,
                getTarget(), Controls.UDP_PORT);
        return p;
    }
}
