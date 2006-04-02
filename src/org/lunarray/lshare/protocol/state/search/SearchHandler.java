package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.search.ResultOut;
import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;
import org.lunarray.lshare.tasks.RunnableTask;

public class SearchHandler implements RunnableTask {

	private InetAddress to;
	private String query;
	
	public SearchHandler(InetAddress t, String q) {
		to = t;
		query = q;
	}
	
	public void runTask(Controls c) {
		for (SharedDirectory d: c.getState().getShareList().getShares()) {
			for (SharedFile f: d.getMatchingFiles(query)) {
				// TODO
				ResultOut o = new ResultOut(to, f);
				c.getUDPTransport().send(o);
			}
		}
	}

}
