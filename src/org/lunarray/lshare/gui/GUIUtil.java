package org.lunarray.lshare.gui;

import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;

public class GUIUtil {

	public static String prettyPrint(long n) {
		String[] units = {"B" ,"KB", "MB", "GB"};
		int i = 0;
		while (n > 9999 && i < units.length) {
			n = n / 1024;
			i++;
		}
		return Long.valueOf(n).toString() + " " + units[i];
	}
	
	public static String hashToString(byte[] dat) {
		String ret = "";
		for (byte b: dat) {
			ret += quadBitToString(b) + quadBitToString(b >> 4);
		}
		return ret;
	}
	
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
	
	public static String stripDirPath(String path) {
		if (path.contains(SharedDirectory.SEPARATOR)) {
			return path.substring(0, path.lastIndexOf(SharedDirectory.SEPARATOR));
		} else {
			return "";
		}
	}
}
