package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;

/**
 * A specific node of a users filelist.
 * @author Pal Hargitai
 */
public class ListNode {

	/**
	 * The file entry that is represented in the node.
	 */
	private FilelistEntry entry;
	
	/**
	 * The entries in this node.
	 */
	private ArrayList<ListNode> children;
	
	/**
	 * Constructs the node with a specified entry.
	 * @param e The entry to be represented by this node.
	 */
	public ListNode(FilelistEntry e) {
		entry = e;
		children = null;
	}
	
	/**
	 * Checks if the current node is a leaf (file) or not (directory).
	 * @return True if the node is a file (ie. leaf) false of the node is a
	 * directory.
	 */
	public boolean isLeaf() {
		return entry.isFile();
	}
	
	/**
	 * Gets the amount of children in this node.
	 * @return The amount of children in this node.
	 */
	public int size() {
		if (children == null) {
			popChildren(); 
		}
		return children.size();
	}
	
	/**
	 * Gets a specified child.
	 * @param i The index of the child.
	 * @return The child.
	 */
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
	
	/**
	 * Gets the index of a specified child.
	 * @param n The node to get the index of.
	 * @return The index of the node.
	 */
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
	
	/**
	 * Gets the file size of the entry represented in this node.
	 * @return The size of the node.
	 */
	public long getFileSize() {
		return entry.getSize(); 
	}
	
	/**
	 * Gets the last modified date of the entry represented in this node.
	 * @return The last modified date of this node.
	 */
	public long getLastModified() {
		return entry.getLastModified();
	}
	
	/**
	 * Gets the name of the entey represented in this node.
	 * @return The name of the node.
	 */
	public String getName() {
		return entry.getName();
	}
	
	/**
	 * Gets a string representation of the entry represented in this node.
	 * @return The string representation of this entry.
	 */
	public String getHash() {
		if (entry.hasHash()) {
			return GUIUtil.hashToString(entry.getHash());
		} else {
			return "";
		}
	}
	
	/**
	 * The string representation of this node.
	 * @return The string representation.
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Gets the file entry associated with the entry in this node.
	 * @return The entry represented in this node.
	 */
	protected FilelistEntry getEntry() {
		return entry;
	}
	
	/**
	 * Populates the child entries in this node.
	 */
	private void popChildren() {
		children = new ArrayList<ListNode>();
		for (FilelistEntry e: entry.getEntries()) {
			children.add(new ListNode(e));
		}
	}
}
