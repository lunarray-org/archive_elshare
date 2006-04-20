package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.state.userlist.ExternalUserList;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.AbstractTreeTableModel;
import com.sun.swing.TreeTableModel;

public class ListModel extends AbstractTreeTableModel implements 
		TreeTableModel {
	/** The model listeners.
	 */
	private ArrayList<TreeModelListener> listeners;
	
	/** The file list that this model is used in.
	 */
	private FileList list;
	
	/** Constructs a list.
	 * @param l The userlist that the filelisti s fetched from.
	 * @param u The user that the filelist is to be fetched from.
	 * @param f The file list that this model is used in.
	 */
	public ListModel(ExternalUserList l, User u, FileList f) {
		super(new ListNode(l.getFilelist(u), null, null));
		((ListNode)getRoot()).setModel(this);
		listeners = new ArrayList<TreeModelListener>();
		
		list = f;
	}
	
	/** Removes a model listener.
	 * @param arg0 The listener to be removed.
	 */
	public void removeTreeModelListener(TreeModelListener arg0) {
		listeners.remove(arg0);
	}
	
	/** Adds a model listener.
	 * @param arg0 The listener to be added.
	 */
	public void addTreeModelListener(TreeModelListener arg0) {
		listeners.add(arg0);
	}
	
	/** Gets a the child at of the specified node at the specified index.
	 * @param arg0 The parent.
	 * @param arg1 The index of the child.
	 * @return The child.
	 */
	public Object getChild(Object arg0, int arg1) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).get(arg1);
		} else {
			return null;
		}
	}
	
	/** Gets the amount of children the node has.
	 * @param arg0 The node to get the count of.
	 */
	public int getChildCount(Object arg0) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).size();
		} else {
			return 0;
		}
	}
	
	/** Gets the index of the child in the parent.
	 * @param arg0 The parent.
	 * @param arg1 The child.
	 * @return The index of the child in the parent.
	 */
	public int getIndexOfChild(Object arg0, Object arg1) {
		if (arg0.getClass().equals(ListNode.class) && arg1.getClass().equals(
				ListNode.class)) {
			return ((ListNode)arg0).getIndex((ListNode)arg1);
		} else {
			return -1;
		}
	}
	
	/** Gets the class of the spcified column.
	 * @param column The column whose class to get.
	 * @return The class of the specified column.
	 */
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class;
		default:
			return String.class;
		}
	}
	
	/** Gets the amount of columns.
	 * @return The amount of columns. (Generally 4)
	 */
	public int getColumnCount() {
		return 4;
	}
	
	/** Gets the name of the specified column.
	 * @param column The column index.
	 * @return The name of the specified column.
	 */
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Name";
		case 1:
			return "Size";
		case 2:
			return "Last Modified Date";
		case 3:
			return "File hash";
		default:
			return "";
		}	
	}
	
	/** Gets the value of the node in the specific column.
	 * @param node The node to get the value of.
	 * @param column The column to get the value of.
	 * @return The value at the specified node and column.
	 */
	public Object getValueAt(Object node, int column) {
		if (node.getClass().equals(ListNode.class)) {
			ListNode n = (ListNode)node;
			switch (column) {
			case 0:
				return n.getName();
			case 1:
				if (n.getFileSize() >= 0) {
					return GUIUtil.prettyPrint(n.getFileSize());
				} else {
					return "";
				}
			case 2:
				if (n.getLastModified() == 0) {
					return "";
				} else {
					return new Date(n.getLastModified());
				}
			case 3:
				return n.getHash();
			default:
				return "";
			}
		} else {
			return null;
		}
	}
	
	/** Checks wether the specified node is a leaf.
	 * @param arg0 The node to check.
	 * @return True if the node is a leaf, false if not.
	 */
	public boolean isLeaf(Object arg0) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).isLeaf();
		} else {
			return false;
		}
	}
	
	/** Updates the given node, ie. tells listeners that children have bene
	 * inserted.
	 * @param n The node to update.
	 */
	protected void updateNode(ListNode n) {
		TreePath p = new TreePath(getPathToRoot(n, 0));
		
		TreeModelEvent e = new TreeModelEvent(n, p);
		for (TreeModelListener l: listeners) {
			l.treeStructureChanged(e);
		}
		
		list.updatedModel();
	}
	
	/** Gets the path from this node to the root node.
	 * @param node The node to get the path of.
	 * @param depth The current depth.
	 * @return The path from the given node to the root.
	 */
	protected ListNode[] getPathToRoot(ListNode node, int depth) {
		if (node == null) {
			if (depth == 0) {
				return null;
			}
			return new ListNode[depth];
		}
		ListNode[] path = getPathToRoot(node.getParent(), depth + 1);
		path[path.length - depth - 1] = node;
		return path;
	}
}
