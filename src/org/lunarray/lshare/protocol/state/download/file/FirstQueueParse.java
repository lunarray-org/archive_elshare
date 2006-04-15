package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class FirstQueueParse implements RunnableTask {

	private DownloadManager manager;
	private LinkedBlockingQueue<QueuedItem> files;
	private boolean shouldrun;
	private DownloadFileManager filemanager;
	
	public FirstQueueParse(DownloadManager m, DownloadFileManager f) {
		files = new LinkedBlockingQueue<QueuedItem>();
		manager = m;
		shouldrun = true;
		filemanager = f;
	}
	
	public void toParse(RemoteFile f, User u, File todir) {
		files.add(new QueuedItem(f, u, todir));
	}
	
	public void close() {
		shouldrun = false;
	}
	
	public void recurseIntoDir(String tol, RemoteFile f) {
		// TODO
	}
	
	public void runTask(Controls c) {
		run: {
			while (true) {
				try {
					QueuedItem i = files.take();
					if (i.getFile().isFile()) {
						File f = new File(i.getTargetDir() + File.
								pathSeparator + i.getFile().getName());
						
						try {
							IncompleteFile inc;
							if (filemanager.fileExists(f)) {
								inc = filemanager.getFile(f);
							} else {
								inc = filemanager.newFile(f, i.getFile().getSize());
							}

							if (inc.matches(i.getFile())) {
								// TODO setup
								manager.enqueue(inc);
							}							
						} catch (FileNotFoundException fnfe) {
							// File not found, should not occur
						} catch (FileExistsException fee) {
							// TODO log
						}
					} else {
						// TODO do recurse stuff
					}
				} catch (InterruptedException ie) {
					// Ignore
				}
				if (!shouldrun) {
					break run;
				}
			}
		}
	}
	
	private class QueuedItem {
		private RemoteFile file;
		private User user;
		private File targetdir;
		
		public QueuedItem(RemoteFile f, User u, File todir) {
			file = f;
			user = u;
			targetdir = todir;
		}
		
		public RemoteFile getFile() {
			return file;
		}
		
		public User getUser() {
			return user;
		}
		
		public File getTargetDir() {
			return targetdir;
		}
	}
}
