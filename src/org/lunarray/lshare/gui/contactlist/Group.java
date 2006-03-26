package org.lunarray.lshare.gui.contactlist;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.lunarray.lshare.protocol.state.User;

public class Group implements TreeNode {
	
	private Root root;
	private String name;
	private Vector<UserNode> ulist;
	
	public Group(Root r, String n) {
		root = r;
		name = n;
		ulist = new Vector<UserNode>();
	}
	
	protected UserNode addUser(User u) {
		UserNode n = new UserNode(this, u);
		ulist.add(n);
		return n;
	}
	
	protected UserNode removeUser(User u) {
		UserNode torem = null;
		search: {
			for (UserNode un: ulist) {
				if (un.getUser() == u) {
					torem = un;
					break search;
				}
			}
		}
		if (torem != null) {
			ulist.remove(torem);
		}
		return torem;
	}
	
	protected UserNode findUser(User u) {
		for (UserNode un: ulist) {
			if (un.getUser() == u) {
				return un;
			}
		}
		return null;
	}

	public UserNode getChildAt(int arg0) {
		if (arg0 >= 0 && arg0 < ulist.size()) {
			return ulist.get(arg0);
		} else {
			return null;
		}
	}

	public int getChildCount() {
		return ulist.size();
	}

	public Root getParent() {
		return root;
	}

	public int getIndex(TreeNode arg0) {
		if (ulist.contains(arg0)) {
			return ulist.indexOf(arg0);
		} else {
			return -1;
		}
	}

	public int getIndex(Object arg0) {
		if (ulist.contains(arg0)) {
			return ulist.indexOf(arg0);
		} else {
			return -1;
		}
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public Enumeration<UserNode> children() {
		return ulist.elements();
	}

	public String toString() {
		return name;
	}
}
