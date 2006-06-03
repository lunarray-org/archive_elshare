package org.lunarray.lshare.protocol.state.userlist;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

/**
 * The task for keeping an updated list of online users.
 * @author Pal Hargitai
 */
public class UserListTask extends TimedRunnableTask {
    /**
     * The amount of milliseconds after which a user is concidered inactive.
     * This is: {@value}.
     */
    public final static long USER_TO = 60000;

    /**
     * The time of the last check.
     */
    private long lasttime;

    /**
     * Constructs a user list task.
     */
    public UserListTask() {
        lasttime = System.currentTimeMillis();
    }

    @Override
    /**
     * Get the time between a check.
     */
    public int getDelay() {
        return 1000;
    }

    @Override
    /**
     * Runs the check for timed out users.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        long curtime = System.currentTimeMillis();
        long diff = curtime - lasttime;
        lasttime = curtime;
        ArrayList<User> timedout = new ArrayList<User>();
        for (User u : c.getState().getUserList().getUserList()) {
            if (u.isOnline()) {
                long ud = u.addDiff(diff);
                if (ud > USER_TO) {
                    timedout.add(u);
                }
            }
        }
        for (User u : timedout) {
            c.getState().getUserList().signoffUser(u.getAddress());
        }

        Controls.getLogger().finest(
                "Checked timeouts at " + Long.valueOf(curtime).toString());
    }
}
