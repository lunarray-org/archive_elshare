package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;

import org.lunarray.lshare.gui.GUIUtil;
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
			return GUIUtil.hashToString(entry.getHash());
		}
	}
	
	public String toString() {
		return getName();
	}
}
