package org.lunarray.lshare.gui;

import org.lunarray.lshare.protocol.state.sharing.ShareList;

/**
 * Some standard functions to allow an more appealing representation of the
 * data given in this protocol.
 * @author Pal Hargitai
 */
public class GUIUtil {

	/**
	 * Gives a short and readable version of the file size.
	 * @param n The number (size) to pretty print.
	 * @return A pretty printed version of the given size.
	 */
	public static String prettyPrint(long n) {
		String[] units = {"B" ,"KB", "MB", "GB"};
		int i = 0;
		while (n > 9999 && i < units.length) {
			n = n / 1024;
			i++;
		}
		return Long.valueOf(n).toString() + " " + units[i];
	}
	
	/**
	 * Gives a readable string version of a given hash.
	 * @param dat The hash to make readable.
	 * @return A readable version of dat.
	 */
	public static String hashToString(byte[] dat) {
		String ret = "";
		for (byte b: dat) {
			ret += quadBitToString(b) + quadBitToString(b >> 4);
		}
		return ret;
	}
	
	/**
	 * Gives a HEX representation of a given 4 bit number. 
	 * @param b The b number to convert.
	 * @return The string representation of the last 4 bits of b.
	 */
	private static String quadBitToString(int b) {
		switch (b & 0x0F) {
		case 0x0:
			return "0";
		case 0x1:
			return "1";
		case 0x2:
			return "2";
		case 0x3:
			return "3";
		case 0x4:
			return "4";
		case 0x5:
			return "5";
		case 0x6:
			return "6";
		case 0x7:
			return "7";
		case 0x8:
			return "8";
		case 0x9:
			return "9";
		case 0xA:
			return "A";
		case 0xB:
			return "B";
		case 0xC:
			return "C";
		case 0xD:
			return "D";
		case 0xE:
			return "E";
		case 0xF:
			return "F";
		default:
			return "";
		}
	}
	
	/**
	 * Strips the last element off the given dir path.
	 * @param path The path to strip.
	 * @return The path with the last path element stripped.
	 */
	public static String stripDirPath(String path) {
		if (path.contains(ShareList.SEPARATOR)) {
			return path.substring(0, path.lastIndexOf(ShareList.SEPARATOR));
		} else {
			return "";
		}
	}
}
