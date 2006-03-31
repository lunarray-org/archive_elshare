package org.lunarray.lshare.gui.contactlist;

import java.util.Vector;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.state.userlist.User;

public class Root {
		
	private Group buddies;
	private Group online;
	private Group offline;
	private Vector<Group> items;
	private LShare lshare;
	
	public Root(LShare ls) {
		lshare = ls;
		buddies = new Group("Online Buddies");
		online = new Group("Online Users");
		offline = new Group("Offline Buddies");
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
		switch (arg0) {
		case 0:
			return buddies;
		case 1:
			return online;
		case 2:
			return offline;
		default:
			return null;
		}
	}

	public int getChildCount() {
		return 3;
	}

	public int getIndex(Object arg0) {
		if (buddies == arg0) {
			return 0;
		} else if (online == arg0) {
			return 1;
		} else if (offline == arg0) {
			return 2;
		} else {
			return -1;
		}
	}

	public String toString() {
		return "Contact List";
	}
}
