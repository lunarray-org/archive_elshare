package org.lunarray.lshare.gui.contactlist;

import java.util.ArrayList;
import java.util.Collections;

import org.lunarray.lshare.protocol.state.userlist.User;

public class Group {
	
	private String name;
	private ArrayList<UserNode> ulist;
	
	public Group(String n) {
		name = n;
		ulist = new ArrayList<UserNode>();
	}
	
	protected UserNode addUser(User u) {
		UserNode n = new UserNode(this, u);
		int i = Collections.binarySearch(ulist, u) + 1;
		ulist.add(i, n);
		return n;
	}
	
	protected UserNode removeUser(User u) {
		UserNode toret = null;
		int i = Collections.binarySearch(ulist, u);
		if (0 <= i && i < ulist.size()) {
			if (ulist.get(i).compareTo(u) == 0) {
				toret = ulist.get(i);
				ulist.remove(i);
			}
		}
		return toret;
	}
	
	protected UserNode findUser(User u) {
		int i = Collections.binarySearch(ulist, u);
		if (0 <= i && i < ulist.size()) {
			if (ulist.get(i).compareTo(u) == 0) {
				return ulist.get(i);
			}
		}
		return null;
	}

	public UserNode getChildAt(int arg0) {
		if (0 <= arg0 && arg0 < ulist.size()) {
			return ulist.get(arg0);
		} else {
			return null;
		}
	}

	public int getChildCount() {
		return ulist.size();
	}

	public int getIndex(Object arg0) {
		if (ulist.contains(arg0)) {
			return ulist.indexOf(arg0);
		} else {
			return -1;
		}
	}

	public String toString() {
		return name;
	}
}
