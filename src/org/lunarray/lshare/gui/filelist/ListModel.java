package org.lunarray.lshare.gui.filelist;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.event.TreeModelListener;

import org.lunarray.lshare.protocol.state.userlist.ExternalUserList;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.AbstractTreeTableModel;
import com.sun.swing.TreeTableModel;

public class ListModel extends AbstractTreeTableModel implements TreeTableModel {
	
	private ArrayList<TreeModelListener> listeners;
	
	public ListModel(ExternalUserList l, User u) {
		super(new ListNode(l.getFilelist(u)));
		listeners = new ArrayList<TreeModelListener>();
	}
	
	public void removeTreeModelListener(TreeModelListener arg0) {
		listeners.remove(arg0);
	}
	
	public void addTreeModelListener(TreeModelListener arg0) {
		listeners.add(arg0);
	}
	
	public Object getChild(Object arg0, int arg1) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).get(arg1);
		} else {
			return null;
		}
	}
	
	public int getChildCount(Object arg0) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).size();
		} else {
			return 0;
		}
	}
	
	public int getIndexOfChild(Object arg0, Object arg1) {
		if (arg0.getClass().equals(ListNode.class) && arg1.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).getIndex((ListNode)arg1);
		} else {
			return -1;
		}
	}
	
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		default:
			return null;
		}
	}
	
	public int getColumnCount() {
		return 4;
	}
	
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
	
	public Object getValueAt(Object node, int column) {
		if (node.getClass().equals(ListNode.class)) {
			ListNode n = (ListNode)node;
			switch (column) {
			case 0:
				return n.getName();
			case 1:
				if (n.getFileSize() >= 0) {
					return prettyPrint(n.getFileSize());
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
	
	public boolean isLeaf(Object arg0) {
		if (arg0.getClass().equals(ListNode.class)) {
			return ((ListNode)arg0).isLeaf();
		} else {
			return false;
		}
	}
	
	public String prettyPrint(long n) {
		String[] units = {"B" ,"KB", "MB", "GB"};
		int i = 0;
		while (n > 9999 && i < units.length) {
			n = n / 1024;
			i++;
		}
		return Long.valueOf(n).toString() + " " + units[i];
	}
}
