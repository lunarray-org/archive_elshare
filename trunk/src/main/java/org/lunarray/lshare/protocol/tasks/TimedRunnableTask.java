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

import java.util.TimerTask;

import org.lunarray.lshare.protocol.Controls;

/**
 * A timed runnable task.
 * @author Pal Hargitai
 */
public abstract class TimedRunnableTask extends TimerTask implements
        RunnableTask {
    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * Gets the delay of between executions of the task.
     * @return The delay between executions of the task.
     */
    public abstract int getDelay();

    /**
     * Set the controls of the protocol.
     * @param c The controls of the protocol.
     */
    public void setControls(Controls c) {
        controls = c;
    }

    @Override
    /**
     * Runs the task.
     */
    public void run() {
        runTask(controls);
    }

    /**
     * Runs the actual task.
     * @param c The controls to the protocol.
     */
    public abstract void runTask(Controls c);
}
