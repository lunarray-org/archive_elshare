package org.lunarray.lshare.protocol.state;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.tasks.TimedRunnableTask;

public class UserListTask extends TimedRunnableTask {

	public static long USER_TO = 60000;
	
	private long lasttime;
	
	public UserListTask() {
		lasttime = System.currentTimeMillis();
	}
	
	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public void runTask(Controls c) {
		long curtime = System.currentTimeMillis();
		long diff = curtime - lasttime;
		lasttime = curtime;
		ArrayList<User> timedout = new ArrayList<User>();
		for (User u: c.getState().getUserList().getUserList()) {
			if (u.isOnline()) {
				long ud = u.addDiff(diff);
				if (ud > USER_TO) {
					timedout.add(u);
				}
			}
		}
		for (User u: timedout) {
			c.getState().getUserList().signoffUser(u.getAddress());
		}
		
		Controls.getLogger().finer("UserListTask: checked timeouts at " + 
				Long.valueOf(curtime).toString());
	}
}
