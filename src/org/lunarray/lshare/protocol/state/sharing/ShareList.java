package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;

public class ShareList implements ExternalShareList {
	
	public static String SEPARATOR = "/";
	private TreeMap<String, File> shares;
	private ShareSettings settings;
	private boolean ishashing;
	
	public ShareList(Controls c) {
		shares = new TreeMap<String, File>();
		settings = c.getSettings().getShareSettings();
		ishashing = false;
		init();
	}
	
	public void addShare(String sname, File fpath) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeShare(String sname) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void init() {
		for (String s: settings.getShareNames()) {
			File f = new File(settings.getSharePath(s));
			if (f.exists() && f.isDirectory() && !f.isHidden()) {
				shares.put(s, f);
			} else {
				settings.removeSharePath(s);
			}
		}
	}
	
	public ShareEntry getEntryFor(File f) throws FileNotFoundException {
		for (String skey: shares.keySet()) {
			if (f.getPath().startsWith(shares.get(skey).getPath())) {
				String rewritten = "." + SEPARATOR + skey + f.getPath().
						substring(shares.get(skey).getPath().length()).
						replace(File.pathSeparator, SEPARATOR);
				int i = rewritten.lastIndexOf(SEPARATOR);
				rewritten = rewritten.substring(0, i);
				return new ShareEntry(f, f.getName(), rewritten, settings);
			}
		}
		throw new FileNotFoundException();
	}
	
	public List<ShareEntry> getEntriesMatching(String s) {
		ArrayList <ShareEntry> entries = new ArrayList<ShareEntry>();
		for (String skey: shares.keySet()) {
			entries.addAll(getEntriesMatching(s, shares.get(skey), "." + SEPARATOR + skey));
		}
		return entries;
	}
	
	private List<ShareEntry> getEntriesMatching(String s, File f, String path) {
		ArrayList<ShareEntry> entries = new ArrayList<ShareEntry>();
		for (File e: f.listFiles()) {
			if (!e.isHidden() && e.getName().contains(s)) {
				entries.add(new ShareEntry(e, e.getName(), path, settings));
			}
			if (e.isDirectory()) {
				entries.addAll(getEntriesMatching(s, e, path + SEPARATOR + e.getName()));
			}
		}
		return entries;
	}
	
	public List<ShareEntry> getChildrenIn(String path) throws FileNotFoundException {
		ArrayList <ShareEntry> entries = new ArrayList<ShareEntry>();
		String[] split = path.split(SEPARATOR);
		
		if (split.length > 0) {
			if (split[0].equals(".")) {
				if (split.length > 1) {
					if (shares.containsKey(split[1])) {
						entries.addAll(fetchDirEntries(split, 2, shares.get(split[1]), path));
					} else {
						throw new FileNotFoundException();
					}
				} else {
					entries.addAll(getBaseEntries());
				}
			} else {
				throw new FileNotFoundException();
			}
		} else {
			throw new FileNotFoundException();
		}
		return entries;
	}
	
	private List<ShareEntry> fetchDirEntries(String[] path, int depth, File f, String sp) throws FileNotFoundException {
		if (path.length <= depth) {
			ArrayList<ShareEntry> entries = new ArrayList<ShareEntry>();
			// Last element, get children
			for (File e: f.listFiles()) {
				if (!e.isHidden()) {
					entries.add(new ShareEntry(e, e.getName(), sp, settings));
				}
			}
			return entries;
		} else {
			// Just get the children of the matching element
			for (File e: f.listFiles()) {
				if (e.isDirectory() && !e.isHidden() && e.getName().equals(path[depth])) {
					return fetchDirEntries(path, depth + 1, e, sp);
				}
			}
			throw new FileNotFoundException();
		}
	}
	
	public List<ShareEntry> getBaseEntries() {
		ArrayList<ShareEntry> entries = new ArrayList<ShareEntry>();
		for (String n: shares.keySet()) {
			entries.add(new ShareEntry(shares.get(n), n, ".", settings));
		}
		return entries;
	}
	
	private boolean isInShares(File f) {
		for (File s: shares.values()) {
			if (f.getPath().startsWith(s.getPath())) {
				return true;
			}
		}
		return false;
	}
	
	protected synchronized void hash(ShareSettings sset) {
		if (ishashing) {
			return ;
		}
		ishashing = true;
		// Cleanup
		for (String s: sset.getFilesInPath()) {
			File n = new File(s);
			if (!n.exists() || !isInShares(n)) {
				sset.removePath(s);
			}
		}
		for (String s: shares.keySet()) {
			hash(shares.get(s));
		}
		ishashing = false;
	}
	
	private void hash(File f) {
		for (File fl: f.listFiles()) {
			// Hash
			if (fl.isDirectory() && !fl.isHidden()) {
				// Hash children
				hash(fl);
			} else if (fl.isFile() && !fl.isHidden() && fl.canRead()) {
				if (settings.getAccessDate(f.getPath()) < fl.lastModified()) {
					// Update hash
					byte[] h = ShareEntry.hash(fl);
					settings.setData(f.getPath(), h, fl.lastModified());
				}
			}
		}
	}
}
