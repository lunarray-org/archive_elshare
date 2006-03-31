package org.lunarray.lshare.protocol.state.userlist;

import java.net.InetAddress;

public class User implements Comparable<User> {

	private InetAddress address;
	private String name;
	private boolean isbuddy;
	
	public int compareTo(User arg0) {
		if (name.compareTo(arg0.name) > 0) {
			return 1;
		} else if (name.compareTo(arg0.name) < 0) {
			return -1;
		} else if (address.getHostName().compareTo(arg0.address.getHostName()) > 0) {
			return 1;
		} else if (address.getHostName().compareTo(arg0.address.getHostName()) < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public boolean isBuddy() {
		return isbuddy;
	}
	
	public boolean isOnline() {
		return address != null;
	}
	
	public String getHostname() {
		if (address == null) {
			return "<offline>";
		} else {
			return address.getHostName();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setBuddy() {
		
	}
	
	public void unsetBuddy() {
		
	}
}
