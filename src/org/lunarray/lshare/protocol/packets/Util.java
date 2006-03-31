package org.lunarray.lshare.protocol.packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import org.lunarray.lshare.protocol.Controls;

public final class Util {
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
		Charset cset = Charset.forName("UTF-8");
		CharsetDecoder decoder = cset.newDecoder();
		decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		ByteBuffer bytes = ByteBuffer.wrap(data);
		CharBuffer cb = null;
		try {
			cb = decoder.decode(bytes);
		} catch (CharacterCodingException cce) {
			Controls.getLogger().fine("Error encoding " +
					"string (" + cce.getMessage() + ")");
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
		Charset cset = Charset.forName("UTF-8");
		CharsetEncoder encoder = cset.newEncoder();
		encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		ByteBuffer bytes = null;
		CharBuffer cb = CharBuffer.wrap(message.toCharArray());
		try {
			bytes = encoder.encode(cb);
		} catch (CharacterCodingException cce) {
			Controls.getLogger().fine("Error decoding " +
					"string (" + cce.getMessage() + ")");
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

    public static void shortUToByteArray(int value, byte[] array, int
    		offset) {
		array[offset] = (byte) (value >>> 8);
		array[offset + 1] = (byte) value;
	}
    
    public static void longToByteArray(long value, byte[] array, int
    		offset) {
    	array[offset] = (byte) (value >>> 56);
    	array[offset + 1] = (byte) (value >>> 48);
    	array[offset + 2] = (byte) (value >>> 40);
    	array[offset + 3] = (byte) (value >>> 32);
    	array[offset + 4] = (byte) (value >>> 24);
    	array[offset + 5] = (byte) (value >>> 16);
		array[offset + 6] = (byte) (value >>> 8);
		array[offset + 7] = (byte) value;
	}
    
    public static long byteArrayToLong(byte[] array, int offset) {
    	return (
    	(long) ((array[offset] & 0xFF) << 56) +
    	(long) ((array[offset + 1] & 0xFF) << 48) +
    	(long) ((array[offset + 2] & 0xFF) << 40) +
    	(long) ((array[offset + 3] & 0xFF) << 32) +
    	(long) ((array[offset + 4] & 0xFF) << 24) +
    	(long) ((array[offset + 5] & 0xFF) << 16) +
    	(long) ((array[offset + 6] & 0xFF) << 8) +
			(array[offset + 7] & 0xFF));
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
    
    public static short byteArrayToShortU(byte[] array, int offset) {
    	return (short) (((array[offset] & 0xFF) << 8) +
			(array[offset + 1] & 0xFF));
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
}
