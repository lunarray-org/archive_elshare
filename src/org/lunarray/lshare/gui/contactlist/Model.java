package org.lunarray.lshare.gui.contactlist;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.events.UserEvent;
import org.lunarray.lshare.protocol.events.UserListener;

public class Model implements TreeModel,UserListener {
	
	private LShare lshare;
	private Root root;
	private ArrayList<TreeModelListener> listener;
	
	public Model(LShare ls) {
		lshare = ls;
		root = new Root(lshare);
		listener = new ArrayList<TreeModelListener>();
		lshare.getUserList().addListener(this);
	}

	public Root getRoot() {
		return root;
	}

	public Object getChild(Object arg0, int arg1) {
		if (arg0 == root) {
			return root.getChildAt(arg1);
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			if (root.getChildAt(i) == arg0) {
				return root.getChildAt(i).getChildAt(arg1);
			}
		}
		return null;
	}

	public int getChildCount(Object arg0) {
		if (arg0 == root) {
			return root.getChildCount();
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			if (arg0 == root.getChildAt(i)) {
				return root.getChildAt(i).getChildCount();
			}
		}
		return 0;
	}

	public boolean isLeaf(Object arg0) {
		if (arg0 == root) {
			return false;
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			if (arg0 == root.getChildAt(i)) {
				return false;
			}
		}
		return true;
	}

	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// Ignore
	}

	public int getIndexOfChild(Object arg0, Object arg1) {
		if (arg0 == root) {
			if (arg1.getClass().equals(TreeNode.class)) {
				return root.getIndex((TreeNode)arg1);
			} else {
				return -1;
			}
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			if (arg0 == root.getChildAt(i)) {
				return root.getChildAt(i).getIndex(arg1);
			}
		}
		return -1;
	}

	public void addTreeModelListener(TreeModelListener arg0) {
		listener.add(arg0);
	}

	public void removeTreeModelListener(TreeModelListener arg0) {
		listener.remove(arg0);
	}
	
	public void signoff(UserEvent e) {
		if (e.getUser().isBuddy()) {
			fireInsert(root.getChildAt(2).addUser(e.getUser()));
			int i = root.getChildAt(0).getIndex(root.getChildAt(0).findUser(e.
					getUser()));
			fireRemove(root.getChildAt(0).removeUser(e.getUser()), i);
		} else {
			int i = root.getChildAt(1).getIndex(root.getChildAt(1).findUser(e.
					getUser()));
			fireRemove(root.getChildAt(1).removeUser(e.getUser()), i);
		}
	}
	
	public void signon(UserEvent e) {
		if (e.getUser().isBuddy()) {
			int i = root.getChildAt(2).getIndex(root.getChildAt(2).findUser(e.
					getUser()));
			fireRemove(root.getChildAt(2).removeUser(e.getUser()), i);
			fireInsert(root.getChildAt(0).addUser(e.getUser()));
		} else {
			fireInsert(root.getChildAt(1).addUser(e.getUser()));
		}
	}
	
	public void update(UserEvent e) {
		UserNode n;
		if (e.getUser().isBuddy()) {
			n = root.getChildAt(0).findUser(e.getUser());
		} else {
			n = root.getChildAt(1).findUser(e.getUser());
		}
		if (n != null) {
			fireUpdate(n);
		}
	}
	
	private void fireUpdate(UserNode n) {
		TreeModelEvent e = genEvent(n, n.getParent().getIndex(n));
		
		for (TreeModelListener l: listener) {
			l.treeStructureChanged(e);
		}
	}
	
	private void fireInsert(UserNode n) {
		TreeModelEvent e = genEvent(n, n.getParent().getIndex(n));
		
		for (TreeModelListener l: listener) {
			l.treeNodesInserted(e);
		}
	}
	
	private void fireRemove(UserNode n, int i) {
		int[] j = {i};
		Object[] children = {n};
		Object[] p = {root, n.getParent()};
		
		TreeModelEvent e =  new TreeModelEvent(n.getParent(), p, j, children);		
		
		for (TreeModelListener l: listener) {
			l.treeNodesRemoved(e);
		}
	}
	
	private TreeModelEvent genEvent(UserNode n, int i) {
		int[] j = {i};
		Object[] children = {n};
		Object[] p = {root, n.getParent()};
		
		return new TreeModelEvent(n.getParent(), p, j, children);		
	}

	public void toggleBuddy(UserNode u) {
		if (u.getUser().isBuddy()) {
			 
			if (u.getUser().isOnline()) {
				int i = root.getChildAt(0).getIndex(u);	
				fireRemove(root.getChildAt(0).removeUser(u.getUser()), i);
				fireInsert(root.getChildAt(1).addUser(u.getUser()));
			} else {
				int i = root.getChildAt(2).getIndex(u);
				fireRemove(root.getChildAt(2).removeUser(u.getUser()), i);
			}
			u.getUser().unsetBuddy();
		} else {
			int i = root.getChildAt(1).getIndex(u);
			fireRemove(root.getChildAt(1).removeUser(u.getUser()), i);
			fireInsert(root.getChildAt(0).addUser(u.getUser()));
			u.getUser().setBuddy();
		}
	}
}
