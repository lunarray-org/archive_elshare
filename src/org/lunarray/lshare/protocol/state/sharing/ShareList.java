package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;

public class ShareList implements ExternalShareList {
	
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
	
	public void addShare(String name, File location) {
		pathmap.put(name, new SharedDirectory(location, name, settings.getShareSettings()));
		if (location.isDirectory() && location.exists()) {
			settings.getShareSettings().setSharePath(name, location.getAbsolutePath());
		}
	}
	
	public Set<String> getShareNames() {
		return pathmap.keySet();
	}

	public SharedDirectory getShareByName(String name) {
		return pathmap.get(name);
	}
	
	public Collection<SharedDirectory> getShares() {
		return pathmap.values();
	}
	
	public void removeShare(String name) {
		pathmap.remove(name);
		settings.getShareSettings().removeSharePath(name);
	}
	
	private boolean isInShares(File f) {
		for (SharedDirectory s: getShares()) {
			if (f.getPath().startsWith(s.getFilePath())) {
				return true;
			}
		}
		return false;
	}
	
	protected synchronized void hash(ShareSettings sset) {
		// Cleanup
		for (String s: sset.getFilesInPath()) {
			File n = new File(s);
			if (!n.exists() || !isInShares(n)) {
				sset.removePath(s);
			}
		}
		for (SharedDirectory s: getShares()) {
			s.hash();
		}
	}
	
}
