/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.protocol.state.userlist;

import java.util.LinkedList;
import java.util.List;

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
        List<User> timedout = new LinkedList<User>();
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
