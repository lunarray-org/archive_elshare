package org.lunarray.lshare.gui.contactlist;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.events.UserEvent;
import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.state.User;

public class Root implements TreeNode,UserListener {
		
	private Group buddies;
	private Group online;
	private Group offline;
	private Vector<Group> items;
	private LShare lshare;
	private DefaultTreeModel defaultmodel;
	
	public Root(LShare ls) {
		lshare = ls;
		//lshare.getUserList().addListener(this);
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
	
	public void signoff(UserEvent e) {
		if (e.getUser().isBuddy()) {
			insert(offline, e.getUser());
			remove(buddies, e.getUser());
		} else {
			remove(online, e.getUser());
		}
	}
	
	public void signon(UserEvent e) {
		if (e.getUser().isBuddy()) {
			remove(offline, e.getUser());
		} else {
			insert(online, e.getUser());
		}
	}

	public void update(UserEvent e) {
		if (e.getUser().isBuddy()) {
			changed(buddies, e.getUser());
		} else {
			changed(online, e.getUser());
		}
	}
	
	private void changed(Group g, User u) {
		UserNode n = g.findUser(u);
		defaultmodel.nodeChanged(n);
	}
	
	private void insert(Group g, User u) {
		UserNode n = g.addUser(u);
		int[] j = {g.getIndex(n)};
		Object[] children = {n};
		Object[] p = {this, g};
		
		TreeModelEvent e = new TreeModelEvent(g, p, j, children);
		
		for (TreeModelListener l: defaultmodel.getTreeModelListeners()) {
			l.treeNodesInserted(e);
		}
	}
	
	private void remove(Group g, User u) {
		UserNode n = g.removeUser(u);
		int[] i = {g.getIndex(n)};
		UserNode[] na = {n};
		defaultmodel.nodesWereRemoved(g, i, na);		
	}
	
	public void setModel(DefaultTreeModel dtm) {
		defaultmodel = dtm;
	}
}
