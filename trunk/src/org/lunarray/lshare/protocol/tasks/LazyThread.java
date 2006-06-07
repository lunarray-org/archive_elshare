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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.lunarray.lshare.protocol.Controls;

/**
 * A lazy thread whose job have no priority and only cares about getting them
 * done 'some time'.
 * @author Pal Hargitai
 */
public class LazyThread extends Thread {
    /**
     * The queue of tasks to be done.
     */
    private LinkedBlockingQueue<RunnableTask> queue;

    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * Wether this should run.
     */
    private boolean shouldrun;

    /**
     * The semaphore to synchronise shutdown.
     */
    private Semaphore running;

    /**
     * Constructs the lazy thread.
     * @param c The controls to the protocol.
     */
    public LazyThread(Controls c) {
        controls = c;
        queue = new LinkedBlockingQueue<RunnableTask>();
        shouldrun = true;
        running = new Semaphore(1);
    }

    /**
     * Enqueue a task to be run.
     * @param r The task to run.
     */
    public void enqueue(RunnableTask r) {
        queue.add(r);
    }

    /**
     * Close down this thread.
     */
    public void close() {
        shouldrun = false;
        try {
            running.acquire();
        } catch (InterruptedException ie) {
            // Ignore
        }
        interrupt();
    }

    /**
     * Run the thread and perform all queued tasks.
     */
    public void run() {
        run: {
            while (true) {
                try {
                    RunnableTask r = queue.take();
                    running.acquire();
                    r.runTask(controls);
                    running.release();
                } catch (InterruptedException ie) {
                    // Ignore
                }

                if (!shouldrun) {
                    break run;
                }
            }
        }
    }
}
