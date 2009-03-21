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
 * A task for handling timeout type tasks.
 * @author Pal Hargitai
 */
public abstract class TimeoutTask extends TimedRunnableTask {
    /**
     * The amount of timeouts till cleanup.
     */
    private int togo;

    /**
     * Constructs a timeout task.
     */
    public TimeoutTask() {
        togo = amount();
    }

    @Override
    /**
     * Gets the delay between timeouts.
     */
    public abstract int getDelay();

    /**
     * Cleanup the task incase of all possible timeouts having passed.
     * @param c The controls to the protocol.
     */
    public abstract void cleanupTimedout(Controls c);

    /**
     * Cleanup the task incase it is not relevant.
     * @param c The controls of the protocol.
     */
    public abstract void cleanupFinished(Controls c);

    /**
     * Handle a single passing of a timeout.
     * @param c The controls of the protocol.
     */
    public abstract void timedOut(Controls c);

    /**
     * Checks of the task is still relevant.
     * @param c The controls of the protocol.
     * @return True if it is not relevant, false if it is.
     */
    public abstract boolean finished(Controls c);

    /**
     * The amount of timeouts that are allowed to pas.
     * @return The amount of passable timeouts.
     */
    public abstract int amount();

    @Override
    /**
     * The task of handling a single timeout.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        if (finished(c)) {
            cleanupFinished(c);
            cancel();
        } else if (togo > 0) {
            timedOut(c);
            togo--;
        } else {
            cleanupTimedout(c);
            cancel();
        }
    }
}
