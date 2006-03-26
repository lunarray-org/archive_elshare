package org.lunarray.lshare.protocol.events;

public interface UserListener {

	void signon(UserEvent e);
	
	void signoff(UserEvent e);
	
	void update(UserEvent e);
	
}
