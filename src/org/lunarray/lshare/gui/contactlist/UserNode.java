package org.lunarray.lshare.gui.contactlist;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.lunarray.lshare.protocol.state.User;

public class UserNode implements TreeNode {
	
	private Group group;
	private User user;
	
	public UserNode(Group g, User u) {
		group = g;
		user = u;
	}
	
	public User getUser() {
		return user;
	}

	public TreeNode getChildAt(int arg0) {
		return null;
	}

	public int getChildCount() {
		return 0;
	}

	public Group getParent() {
		return group;
	}

	public int getIndex(TreeNode arg0) {
		return 0;
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public Enumeration children() {
		return null;
	}

	public String toString() {
		return user.getName();
	}
}
