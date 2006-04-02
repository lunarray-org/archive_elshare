package org.lunarray.lshare.protocol.tasks;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.lunarray.lshare.protocol.Controls;

public class LazyThread extends Thread {

	private LinkedBlockingQueue<RunnableTask> queue;
	private Controls controls;
	private boolean shouldrun;
	private Semaphore running;
	
	public LazyThread(Controls c) {
		controls = c;
		queue = new LinkedBlockingQueue<RunnableTask>();
		shouldrun = true;
		running = new Semaphore(1);
	}
	
	public void enqueue(RunnableTask r) {
		queue.add(r);
	}
	
	public void close() {
		shouldrun = false;
		try {
			running.acquire();
		} catch (InterruptedException ie) {
			// Ignore
		}		
		interrupt();
	}
	
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
