package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;
import java.util.Collections;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.filelist.FilelistEntry;

/**
 * A specific node of a users filelist.
 * @author Pal Hargitai
 */
public class ListNode implements Comparable<FilelistEntry> {

	/**
	 * The file entry that is represented in the node.
	 */
	private FilelistEntry entry;
	
	/**
	 * The entries in this node.
	 */
	private ArrayList<ListNode> children;
	
	/**
	 * The parent of this node.
	 */
	private ListNode parent;
	
	/**
	 * The model that this node resides in. 
	 */
	private ListModel model;
	
	/**
	 * A thread for adding entries.
	 */
	private EntryAdder adder;
	
	/**
	 * Constructs the node with a specified entry.
	 * @param e The entry to be represented by this node.
	 * @param p The parent of this node.
	 * @param m The model in which this node resides.
	 */
	public ListNode(FilelistEntry e, ListNode p, ListModel m) {
		entry = e;
		children = null;
		parent = p;
		model = m;
		adder = new EntryAdder();
	}
	
	/**
	 * Set the model for this node.
	 * @param m The model to set it to.
	 */
	public void setModel(ListModel m) {
		model = m;
	}
	
	/**
	 * Gets the parent of this node.
	 * @return The parent of this node.
	 */
	public ListNode getParent() {
		return parent;
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
			return 0;
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
			return null;
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
			return -1;
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
	 * Compare the file list entry of another node to this filelist entry.
	 * @param arg0 The file list entry to compare to.
	 * @return < 0 If the entry in this node is less than the given entry.
	 * > 0 If the entry in this node is greater than the given entry.
	 * = 0 If the entry in this nodes equals the given entry.
	 */
	public int compareTo(FilelistEntry arg0) {
		return entry.compareTo(arg0);
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
	private synchronized void popChildren() {
		if (!adder.isAlive()) {
			adder.start();
		}
	}
	
	/**
	 * Add the given entry to this node.
	 * @param e The entry to add.
	 */
	private void addChild(FilelistEntry e) {
		int i = Collections.binarySearch(children, e);
		if (i < 0) {
			children.add(-(i + 1), new ListNode(e, this, model));
		}
	}
	
	/**
	 * Trigger an update of the model on this node.
	 */
	private void triggerModel() {
		model.updateNode(this);
	}
	
	/**
	 * An entry adder
	 * @author Pal Hargitai
	 */
	private class EntryAdder extends Thread {
		/**
		 * Gets all child nodes and registers this with the model.
		 */
		public void run () {
			children = new ArrayList<ListNode>();
			for (FilelistEntry e: entry.getEntries()) {
				addChild(e);
			}
			triggerModel();
		}
	}
}
