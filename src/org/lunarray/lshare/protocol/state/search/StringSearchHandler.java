package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.search.ResultOut;
import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class StringSearchHandler implements RunnableTask {

	private InetAddress to;
	private String query;
	
	public StringSearchHandler(InetAddress t, String q) {
		to = t;
		query = q;
	}
	
	public void runTask(Controls c) {
		for (SharedDirectory d: c.getState().getShareList().getShares()) {
			for (SharedFile f: d.getMatchingFiles(query)) {
				ResultOut o = new ResultOut(to, f);
				c.getUDPTransport().send(o);
			}
			for (SharedDirectory f: d.getMatchingDirectories(query)) {
				ResultOut o = new ResultOut(to, f);
				c.getUDPTransport().send(o);
			}
			if (d.getName().contains(query)) {
				ResultOut o = new ResultOut(to, d);
				c.getUDPTransport().send(o);
			}
		}
	}

}
