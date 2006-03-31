package org.lunarray.lshare.protocol.state.userlist;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.UserListener;

public class UserList implements ExternalUserList {

	private Controls controls;
	private ArrayList<UserListener> listeners;
	private ArrayList<User> users;
	
	public UserList(Controls c) {
		listeners = new ArrayList<UserListener>();
		controls = c;
	}
	
	public void addListener(UserListener lis) {
		listeners.add(lis);
	}
	
	public void removeListener(UserListener lis) {
		listeners.add(lis);
	}
	
	public List<User> getUserList() {
		return users;
	}
	
	public void signoffUser(InetAddress address) {
		
	}
	
	public void signonUser(String name, InetAddress address, String challenge) {
		
	}
	
}
