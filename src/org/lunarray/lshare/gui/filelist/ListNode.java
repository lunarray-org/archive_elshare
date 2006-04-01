package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.filelist.FilelistEntry;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;

public class ListNode {

	private FilelistEntry entry;
	private ArrayList<ListNode> children;
	
	public ListNode(FilelistEntry e) {
		entry = e;
		children = null;
	}
	
	public boolean isLeaf() {
		return entry.isFile();
	}
	
	protected FilelistEntry getEntry() {
		return entry;
	}
	
	private void popChildren() {
		children = new ArrayList<ListNode>();
		for (FilelistEntry e: entry.getEntries()) {
			children.add(new ListNode(e));
		}
	}
	
	public int size() {
		if (children == null) {
			popChildren(); 
		}
		return children.size();
	}
	
	public ListNode get(int i) {
		if (children == null) {
			popChildren();
		}
		if (0 <= i && i < children.size()) {
			return children.get(i);
		} else {
			return null;
		}
	}
	
	public int getIndex(ListNode n) {
		if (children == null) {
			popChildren();
		}
		if (children.contains(n)) {
			return children.indexOf(n);
		} else {
			return -1;
		}
	}
	
	public long getFileSize() {
		return entry.getSize(); 
	}
	
	public long getLastModified() {
		return entry.getLastModified();
	}
	
	public String getName() {
		return entry.getName();
	}
	
	public String getHash() {
		if (SharedFile.isEmpty(entry.getHash())) {
			return "";
		} else {
			return hashToString(entry.getHash());
		}
	}
	
	public String toString() {
		return getName();
	}
	
	private String hashToString(byte[] dat) {
		String ret = "";
		for (byte b: dat) {
			ret += quadBitToString(b) + quadBitToString(b >> 4);
		}
		return ret;
	}
	
	private String quadBitToString(int b) {
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
}
