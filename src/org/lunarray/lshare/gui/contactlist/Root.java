package org.lunarray.lshare.gui.contactlist;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.state.User;

public class Root implements TreeNode {
		
	private Group buddies;
	private Group online;
	private Group offline;
	private Vector<Group> items;
	private LShare lshare;
	
	public Root(LShare ls) {
		lshare = ls;
		buddies = new Group(this, "Online Buddies");
		online = new Group(this, "Online Users");
		offline = new Group(this, "Offline Buddies");
		items = new Vector<Group>();
		items.add(buddies);
		items.add(online);
		items.add(offline);
		initTree();
	}
	
	private void initTree() {
		for (User u: lshare.getUserList().getUserList()) {
			if (u.isBuddy()) {
				if (u.isOnline()) {
					buddies.addUser(u);
				} else {
					offline.addUser(u);
				}
			} else {
				if (u.isOnline()) {
					online.addUser(u);
				}
			}
		}
	}

	public Group getChildAt(int arg0) {
		if (arg0 >= 0 && arg0 < items.size()) {
			return items.get(arg0);
		}
		return null;
	}

	public int getChildCount() {
		return 3;
	}

	public TreeNode getParent() {
		return null;
	}

	public int getIndex(TreeNode arg0) {
		if (items.contains(arg0)) {
			return items.indexOf(arg0);
		} else {
			return -1;
		}
	}

	public int getIndex(Object arg0) {
		if (items.contains(arg0)) {
			return items.indexOf(arg0);
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

	public Enumeration<Group> children() {
		return items.elements();
	}

	public String toString() {
		return "Contact List";
	}
}
