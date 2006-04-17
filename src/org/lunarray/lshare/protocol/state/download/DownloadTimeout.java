package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.download.RequestOut;
import org.lunarray.lshare.protocol.tasks.TimeoutTask;

public class DownloadTimeout extends TimeoutTask {

	public final static int DELAY = 1000;

	private DownloadHandler handler;
	private RequestOut packet;
	
	public DownloadTimeout(DownloadHandler h, RequestOut p) {
		handler = h;
		packet = p;
	}
	
	@Override
	public int getDelay() {
		return DELAY;
	}
	
	@Override
	public int amount() {
		return 3;
	}
	
	@Override
	public void timedOut(Controls c) {
		c.getUDPTransport().send(packet);
		Controls.getLogger().fine("Timedout request to: " + packet.getTarget().
				getHostAddress());
	}

	@Override
	public void cleanupFinished(Controls c) {
		// Do nothing, just let it clean up
		Controls.getLogger().finer("Running: " + packet.getTarget().
				getHostAddress());
	}
	
	@Override
	public void cleanupTimedout(Controls c) {
		handler.close();
		Controls.getLogger().fine("Cleaning up: " + packet.getTarget().
				getHostAddress());
	}
	
	@Override
	public boolean finished(Controls c) {
		switch (handler.getStatus()) {
		case CONNECTING:
			return false;
		default:
			return true;
		}
	}
}
