package org.lunarray.lshare.protocol.state.upload;

import java.io.IOException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.download.ResponseOut;
import org.lunarray.lshare.protocol.state.download.DownloadRequest;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class UploadHandler implements RunnableTask {

	private UploadManager manager;
	private DownloadRequest request;
	private UploadTransfer transfer;
	private User user;
	
	public UploadHandler(UploadManager m, DownloadRequest r, User u) {
		manager = m;
		request = r;
		user = u;
	}
	
	public void runTask(Controls c) {
		try {
			transfer = new UploadTransfer(c.getState().getShareList().
					getFileForEntry(request), request.getOffset(), manager);

			int port = transfer.init();
			Controls.getLogger().finer("Opened transfer on: " + Integer.
					valueOf(port).toString());
			c.getTasks().backgroundTask(transfer);
			
			// Send response
			try {
				ResponseOut ro = new ResponseOut(request, port, user, request.
						getOffset());
				c.getUDPTransport().send(ro);
				
				// Sleep for a while
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
					// Ignore
				}
				if (!transfer.isDone()) {
					if (!transfer.isRunning()) {
						Controls.getLogger().warning("Transfer not running.");
						transfer.close();
					}
				} else {
					Controls.getLogger().info("Transfer done.");
				}
				
			} catch (UserNotFound nfu) {
				// Ignore this
			}			
		} catch (IOException ie) {
			transfer.close();
			Controls.getLogger().warning("Could transfer!");
		}
	}
}
