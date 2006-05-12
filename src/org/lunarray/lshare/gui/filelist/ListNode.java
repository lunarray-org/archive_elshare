package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTree;

import org.lunarray.lshare.protocol.filelist.FilelistEntry;

/**
 * A specific node of a users filelist.
 * @author Pal Hargitai
 */
public class ListNode implements Comparator<ListNode>, Comparable<ListNode> {
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
     * The sort direction.
     */
    private SortDir dir;

    /**
     * The criteria to sort on. That is, name, date, size or hash.
     */
    private SortType type;

    /**
     * Wether this node is expanded or not.
     */
    private boolean expanded;

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
        dir = SortDir.NOT_SORTED;
        type = SortType.NAME;
        expanded = false;
    }

    /**
     * Set this node to be expanded.
     * @param e Set to true if this node is expanded, false if it's collapsed.
     */
    public void setExpanded(boolean e) {
        expanded = e;
    }

    /**
     * This function expands or collapses a node and it's children depending on
     * wether it was collapsed or expandd in the first place. This is genrerally
     * called after the tree has been sorted.
     * @param t The tree to expand in.
     * @param p The row this node resides in.
     * @return The amount of expanded rows this node has caused.
     */
    public int checkExpansion(JTree t, int p) {
        if (expanded) {
            t.expandRow(p);
            int q = 1;
            for (ListNode n : children) {
                q += n.checkExpansion(t, q);
            }
            return q;
        }
        return 1;
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
        return 0 <= i && i < children.size() ? children.get(i) : null;
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
        return children.contains(n) ? children.indexOf(n) : -1;
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
        return entry.hasHash() ? entry.getHash().toString() : "";
    }

    /**
     * The string representation of this node.
     * @return The string representation.
     */
    public String toString() {
        return getName();
    }

    /**
     * Compare two file list nodes. Returns < 0 If the first entry is greater
     * than the given entry. Returns > 0 If the first entry is greater than the
     * given entry. Returns = 0 If the first entry is greater than the given
     * entry.
     * @param arg0 A file list node.
     * @param arg1 The file list node to compare to.
     * @return As specified above.
     */
    public int compare(ListNode arg0, ListNode arg1) {
        switch (dir) {
        case ASCENDING:
            switch (type) {
            case DATE:
                return Long.valueOf(arg0.entry.getLastModified()).compareTo(
                        arg1.entry.getLastModified());
            case HASH:
                return arg0.entry.getHash().compareTo(arg1.entry.getHash());
            case SIZE:
                return Long.valueOf(arg0.entry.getSize()).compareTo(
                        arg1.entry.getSize());
            case NAME:
            default:
                return arg0.entry.getName().compareTo(arg1.entry.getName());
            }
        case DESCENDING:
            switch (type) {
            case DATE:
                return -Long.valueOf(arg0.entry.getLastModified()).compareTo(
                        arg1.entry.getLastModified());
            case HASH:
                return -arg0.entry.getHash().compareTo(arg1.entry.getHash());
            case SIZE:
                return -Long.valueOf(arg0.entry.getSize()).compareTo(
                        arg1.entry.getSize());
            case NAME:
            default:
                return -arg0.entry.getName().compareTo(arg1.entry.getName());
            }
        case NOT_SORTED:
        default:
            if (children.contains(arg0)) {
                if (children.contains(arg1)) {
                    return children.indexOf(arg1) - children.indexOf(arg0);
                } else {
                    return 1;
                }
            } else {
                if (children.contains(arg1)) {
                    return -1;
                } else {
                    return arg0.compareTo(arg1);
                }
            }
        }
    }

    /**
     * Compare the file list entry of another node to this filelist entry.
     * Returns < 0 If the entry in this node is less than the given entry.
     * Returns > 0 If the entry in this node is greater than the given entry.
     * Returns = 0 If the entry in this nodes equals the given entry.
     * @param arg0 The file list entry to compare to.
     * @return As specified above.
     */
    public int compareTo(ListNode arg0) {
        return entry.compareTo(arg0.entry);
    }

    /**
     * Switch the type of sorting.
     * @param t The type for which sorting has been triggered.
     */
    public void switchOnType(SortType t) {
        if (t.equals(type)) {
            switch (dir) {
            case ASCENDING:
                dir = SortDir.DESCENDING;
                break;
            case DESCENDING:
                dir = SortDir.NOT_SORTED;
                break;
            case NOT_SORTED:
                dir = SortDir.ASCENDING;
                break;
            default:
                dir = SortDir.ASCENDING;
            }
        } else {
            type = t;
            dir = SortDir.ASCENDING;
        }
        if (children != null) {
            for (ListNode n : children) {
                n.switchOnType(t);
            }
            Collections.sort(children, this);
        }
    }

    /**
     * Gets the file entry associated with the entry in this node.
     * @return The entry represented in this node.
     */
    protected FilelistEntry getEntry() {
        return entry;
    }

    /**
     * Set the sorting type. Should be avoided, use switchtype instead.
     * @param t The type to sort to.
     */
    protected void setType(SortType t) {
        type = t;
    }

    /**
     * Set the sort direction. Should be avoided, use switchtype instead.
     * @param d The direction to sort to.
     */
    protected void setDir(SortDir d) {
        dir = d;
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
        ListNode n = new ListNode(e, this, model);
        n.setType(type);
        n.setDir(dir);
        int i = Collections.binarySearch(children, n, this);
        if (i < 0) {
            children.add(~i, n);
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
        public void run() {
            children = new ArrayList<ListNode>();
            for (FilelistEntry e : entry.getEntries()) {
                addChild(e);
            }
            triggerModel();
        }
    }

    /**
     * A sorttype to indicate what data to sort.
     * @author Pal Hargitai
     */
    public enum SortType {
        NAME, SIZE, DATE, HASH
    }

    /**
     * An enum for deciding how to sort.
     * @author Pal Hargitai
     */
    public enum SortDir {
        DESCENDING, NOT_SORTED, ASCENDING
    }

}
