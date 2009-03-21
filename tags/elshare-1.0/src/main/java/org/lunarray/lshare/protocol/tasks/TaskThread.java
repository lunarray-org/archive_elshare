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
package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

/**
 * A seperate thread for performing a task.
 * @author Pal Hargitai
 */
public class TaskThread extends Thread {
    /**
     * The task that is to be run.
     */
    private RunnableTask task;

    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * Constructs a task thread.
     * @param g The group that the thread should reside in.
     * @param r The task that should be run.
     * @param c The controls to the protocol.
     */
    public TaskThread(ThreadGroup g, RunnableTask r, Controls c) {
        super(g, "taskthread");
        task = r;
        controls = c;
    }

    /**
     * Runs the task.
     */
    public void run() {
        task.runTask(controls);
    }
}
