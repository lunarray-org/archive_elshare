package edu.tue.compnet.protocol.packet;

import java.io.File;
import java.net.DatagramPacket;
import java.nio.*;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;

/**
 * The interface describing basic functionality of a packet.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public abstract class Packet {
	// The packet
	DatagramPacket packet;

	/**
	 * Get the packet to send
	 * @return The actual packet to send
	 */
	public DatagramPacket getPacket() {
		return packet;
	}
	
	/**
	 * The handling of a packet of this sort arriving.
	 * @param packet The packet that came.
	 * @param state The state the protocol is in.
	 * @param daemon The daemon for sending.
	 */
	public static void handlePacket(DatagramPacket packet, State state,
			Transport daemon) {
		/*
		 *  This function should do little to nothing and should always be
		 *  overriden.
		 */
	}
	
	/**
	 * Injects the byte array source of length length into the target
	 * byte array at the offset offset.
	 * @param source The source array.
	 * @param length The length of the source array.
	 * @param target The target array.
	 * @param offset The offset of the target array.
	 */
	public static void injectByteArrayIntoByteArray(byte[] source, int
			length, byte[] target, int offset) {
		for (int i = 0; i < length; i++) {
			target[i+offset] = source[i];
		}
	}
	
	/**
	 * Copies a part of a byte array to a new byte array.
	 * @param source Source byte array.
	 * @param length The amount of bytes to copy.
	 * @param offset The offset to start copying at.
	 * @return The copied byte array.
	 */
	public static byte[] getByteArrayFromByteArray(byte[] source, int length,
			int offset) {
		byte[] target = new byte[length];
		for (int i = 0; i < length; i++) {
			target[i] = source[offset + i];
		}
		return target;
	}
	
	/**
	 * Decoes a byte string encoded in ascii to a java string.
	 * @param data The data to convert.
	 * @return The converted data.
	 */
	public static String decode(byte[] data) {
		// Decoding
		Charset cset = Charset.forName("US-ASCII");
		CharsetDecoder decoder = cset.newDecoder();
		decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		ByteBuffer bytes = ByteBuffer.wrap(data);
		CharBuffer cb = null;
		try {
			cb = decoder.decode(bytes);
		} catch (CharacterCodingException cce) {
			Output.err("Error while encoding message!");
		}
		return cb.toString();
	}
	
	/**
	 * Encode a string to a ascii byte array.
	 * @param message The message to encode.
	 * @return The byte array.
	 */
	public static byte[] encode(String message) {
		// Encoding
		Charset cset = Charset.forName("US-ASCII");
		CharsetEncoder encoder = cset.newEncoder();
		encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		ByteBuffer bytes = null;
		CharBuffer cb = CharBuffer.wrap(message.toCharArray());
		try {
			bytes = encoder.encode(cb);
		} catch (CharacterCodingException cce) {
			Output.err("Error while encoding message!");
		}
		return bytes.array();
	}
		
	/**
	 * Place a short in a byte array at a given offset.
	 * @param value The value to convert.
	 * @param array The array to put it in.
	 * @param offset The offset to put it at.
	 */
    public static void shortToByteArray(short value, byte[] array, int
    		offset) {
		array[offset] = (byte) (value >>> 8);
		array[offset + 1] = (byte) value;
	}

    /**
     * Place an integer in a byte array at a given offset.
     * @param val The value to convert.
     * @param array The array to put it in.
     * @param offset The offset to put it at.
     */
    public static void intToByteArray(int val, byte[] array, int offset) {
    	array[offset] = (byte) (val >>> 24);
    	array[offset + 1] = (byte) (val >>> 16);
    	array[offset + 2] = (byte) (val >>> 8);
    	array[offset + 3] = (byte) val;
    }
    
    /**
     * Check if a file exists in the dir that matches the search string.
     * @param dir The directory to search in.
     * @param search The filename to search for.
     * @return Gives the file that matches or a null.
     */
    public static File fileExists(File dir, String search) {
    	File ret = null;
		if (dir != null) {
			// Get file list
			File[] dirc = dir.listFiles();
			search: {
				for (int i = 0; i < dirc.length; i++) {
					// If it's a file
					if (!dirc[i].isDirectory()) {
						// If it's the right name
						if (search.equals(dirc[i].getName())) {
							ret = dirc[i];
							break search;
						}
					}
				}
			}
		}
		return ret;
    }
    
    /**
     * Check if a file exists in the dir that matches the search string.
     * @param dir The directory to search in.
     * @param search The filename to search for.
     * @return Gives the file that matches or a null.
     */
    public static List<File> fileMatch(File dir, String search) {
    	List<File> list = new ArrayList<File>();
		if (dir != null) {
			// Get file list
			File[] dirc = dir.listFiles();
			for (int i = 0; i < dirc.length; i++) {
				// If it's a file
				if (!dirc[i].isDirectory()) {
					// If it's the right name
					if (dirc[i].getName().contains(search)) {
						list.add(dirc[i]);
					}
				}
			}
		}
		return list;
    }
    
    /**
     * Gets an int from a byte array.
     * @param array The array to get it from.
     * @param offset The offset to get it from.
     * @return The rebuilt int.
     */
    public static int byteArrayToInt(byte[] array, int offset) {
    	return ((array[offset] & 0xFF) << 24) +
			((array[offset + 1] & 0xFF) << 16) +
			((array[offset + 2] & 0xFF) << 8) +
			(array[offset + 3] & 0xFF);
    }
    
    /**
     * Get a short from a byte array.
     * @param array The array to get it from.
     * @param offset The offset of the array.
     * @return The short that comes from the array. 
     */
    public static short byteArrayToShort(byte[] array, int offset) {
    	return (short) (((array[offset] & 0xFF) << 8) +
			(array[offset + 1] & 0xFF));
    }
}
