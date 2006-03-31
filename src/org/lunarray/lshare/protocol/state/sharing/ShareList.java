package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;

public class ShareList {
	
	private TreeMap<String, SharedDirectory> pathmap;
	private Settings settings;
	
	public ShareList(Controls c) {
		pathmap = new TreeMap<String, SharedDirectory>();
		settings = c.getSettings();
		init();
	}
	
	private void init() {
		for (String s: settings.getShareSettings().getShareNames()) {
			File f = new File(settings.getShareSettings().getSharePath(s));
			if (f.exists() && f.isDirectory() && !f.isHidden()) {
				pathmap.put(s, new SharedDirectory(f, s, settings.getShareSettings()));
			} else {
				settings.getShareSettings().removeSharePath(s);
			}
		}
	}
	
	public synchronized void addShare(String name, File location) {
		pathmap.put(name, new SharedDirectory(location, name, settings.getShareSettings()));
		if (location.isDirectory() && location.exists()) {
			settings.getShareSettings().setSharePath(name, location.getAbsolutePath());
		}
	}
	
	public synchronized Set<String> getShareNames() {
		return pathmap.keySet();
	}

	public synchronized SharedDirectory getShareByName(String name) {
		return pathmap.get(name);
	}
	
	public synchronized Collection<SharedDirectory> getShares() {
		return pathmap.values();
	}
	
	public synchronized void removeShare(String name) {
		pathmap.remove(name);
		settings.getShareSettings().removeSharePath(name);
	}
	
	protected synchronized void hash(ShareSettings sset) {
		// Cleanup
		for (String s: sset.getFilesInPath()) {
			File n = new File(s);
			if (!n.exists()) {
				sset.removePath(s);
			}
		}
		for (SharedDirectory s: getShares()) {
			s.hash();
		}
	}
	
}
