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
package org.lunarray.lshare.protocol.state.sharing;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

/**
 * The task for hasing the files.
 * @author Pal Hargitai
 */
public class HashTask extends TimedRunnableTask {
    /**
     * The time between a timed task. That is {@value} milliseconds.
     */
    public final static int DELAY = 6000;

    @Override
    /**
     * Gets the delay between hash checks.
     */
    public int getDelay() {
        return DELAY;
    }

    @Override
    /**
     * Runs the hashing task.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        Controls.getLogger().finer("Started file hashing");
        c.getTasks().backgroundTask(new Task());
    }

    /**
     * A hidden class that is run in the background to hash the file list.
     * @author Pal Hargitai
     */
    private class Task implements RunnableTask {
        /**
         * Processes hashing.
         * @param c The controls to the protocol.
         */
        public void runTask(Controls c) {
            c.getState().getShareList()
                    .hash(c.getSettings().getShareSettings());
        }
    }
}
