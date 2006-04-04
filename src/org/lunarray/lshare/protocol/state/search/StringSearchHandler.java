package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.search.ResultOut;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class StringSearchHandler implements RunnableTask {

	private InetAddress to;
	private String query;
	
	public StringSearchHandler(InetAddress t, String q) {
		to = t;
		query = q;
	}
	
	public void runTask(Controls c) {
		for (ShareEntry e: c.getState().getShareList().getEntriesMatching(query)) {
			ResultOut o = new ResultOut(to, e);
			c.getUDPTransport().send(o);
		}
	}
}
