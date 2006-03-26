package org.lunarray.lshare.protocol.state;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;

public class User {
	
	public static int HASH_LENGTH = 20;
	
	private String challenge;
	private InetAddress address;
	private String name;
	private boolean isbuddy;
	private UserList ulist;
	private long time;
	
	public User(String h, InetAddress a, String n, boolean b, UserList l) {
		challenge = h;
		address = a;
		name = n;
		isbuddy = b;
		ulist = l;
		time = 0;
	}
	
	protected long addDiff(long diff) {
		time += diff;
		return time;
	}
	
	protected void bump() {
		Controls.getLogger().finer("User: " + getName() + " bumped time " +
				"from " + Long.valueOf(time).toString());
		time = 0;
	}
	
	protected String getChallenge() {
		return challenge;
	}
	
	protected boolean challengeMatches(String h) {
		if (challenge.length() <= 0) {
			return false;
		} else {
			return challenge.equals(h);
		}
	}
	
	protected void setAddress(InetAddress a) {
		address = a;
	}
	
	protected InetAddress getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String nu) {
		name = nu;
	}
	
	public String getHostname() {
		if (address != null) {
			return address.getHostName();
		} else {
			return "";
		}		
	}
	
	public String getHostaddress() {
		if (address != null) {
			return address.getHostAddress();
		} else {
			return "";
		}
	}
	
	public boolean isBuddy() {
		return isbuddy;
	}
	
	public boolean isOnline() {
		return address != null;
	}
	
	public void setBuddy() {
		ulist.addBuddy(this);
		isbuddy = true;
	}
	
	public void unsetBuddy() {
		ulist.removeBuddy(this);
		isbuddy = false;
	}
}
