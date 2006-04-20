package org.lunarray.lshare.protocol.tasks;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.lunarray.lshare.protocol.Controls;

/** A lazy thread whose job have no priority and only cares about getting them
 * done 'some time'.
 * @author Pal Hargitai
 */
public class LazyThread extends Thread {
	/** The queue of tasks to be done.
	 */
	private LinkedBlockingQueue<RunnableTask> queue;
	
	/** The controls to the protocol.
	 */
	private Controls controls;
	
	/** Wether this should run.
	 */
	private boolean shouldrun;
	
	/** The semaphore to synchronise shutdown.
	 */
	private Semaphore running;
	
	/** Constructs the lazy thread.
	 * @param c The controls to the protocol.
	 */
	public LazyThread(Controls c) {
		controls = c;
		queue = new LinkedBlockingQueue<RunnableTask>();
		shouldrun = true;
		running = new Semaphore(1);
	}
	
	/** Enqueue a task to be run.
	 * @param r The task to run.
	 */
	public void enqueue(RunnableTask r) {
		queue.add(r);
	}
	
	/** Close down this thread.
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
	
	/** Run the thread and perform all queued tasks.
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
