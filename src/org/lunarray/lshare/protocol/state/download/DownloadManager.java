package org.lunarray.lshare.protocol.state.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.file.DownloadFileManager;
import org.lunarray.lshare.protocol.state.download.file.FileExistsException;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/*
 1- add to queue

 2- handle prelim queue to perm queue

 3- check for transfferables

 4- start transfers

 5- transfer

 6- transfer done/canceled

 7- cleanup transfer

 8- check for what has been done

 9- notify listeners

 10- do 3

 11- cleanup when kicked
 * @author Pal Hargitai
 */
public class DownloadManager implements RunnableTask {

	private LinkedBlockingQueue<QueuedItem> tempqueue;
	private ArrayList<IncompleteFile> queue;
	private Controls controls;
	private DownloadFileManager filemanager;
	private boolean shouldrun;
	private ArrayList<DownloadHandler> transfers;

	public DownloadManager(Controls c) {
		controls = c;
		
		transfers = new ArrayList<DownloadHandler>();
		tempqueue = new LinkedBlockingQueue<QueuedItem>();
		queue = new ArrayList<IncompleteFile>();
		filemanager = new DownloadFileManager(c);
		
		for (IncompleteFile f: filemanager.getIncompleteFiles()) {
			queue.add(f);
		}
		// TODO directly request these from second queue parser
		
		controls.getTasks().backgroundTask(this);
	}
	
	protected void removeDownloadHandler(DownloadHandler h) {
		if (transfers.contains(h)) {
			transfers.remove(h);
		}
	}
	
	// TODO get response
	
	public List<DownloadHandler> getTransfers() {
		return transfers;
	}
	
	protected List<IncompleteFile> getQueue() {
		return queue;
	}
	
	public void close() {
		shouldrun = false;
		for (DownloadHandler t: transfers) {
			t.close();
		}
		filemanager.close();
	}
	
	public void enqueue(RemoteFile f, User u, File todir) {
		tempqueue.add(new QueuedItem(f, u, todir));
	}
	
	public void enqueue(RemoteFile f, User u) {
		enqueue(f, u, controls.getSettings().getDownloadSettings().
				getDownloadDirectory());
	}
	
	// TODO put in new class
	public void runTask(Controls c) {
		run: {
			while (true) {
				try {
					QueuedItem i = tempqueue.take();
					
					if (i.getFile().isDirectory()) {
						// TODO recurse
					} else {
						// is file
						File f = i.getTargetFile();
						
						IncompleteFile inc;
						if (filemanager.fileExists(f)) {
							inc = filemanager.getFile(f);
						} else {
							inc = filemanager.newFile(f, i.getFile().
									getSize());
						}
						
						if (inc.matches(i.getFile())) {
							try {
								inc.addSource(i.getUser(), i.getFile());
							} catch (IllegalArgumentException iae) {
								// Ignore this
							} 
							// If adding source fails, don't add to queue
							if (!queue.contains(inc)) {
								queue.add(inc);
							}
						}
					}
				} catch (InterruptedException ie) {
					// Ignore
				} catch (FileNotFoundException nffe) {
					// Ignore, shouldn't happen
				} catch (FileExistsException fee) {
					// Ignore, we don't overwrite
				}
				if (!shouldrun) {
					break run;
				}
			}
		}
	}
}
