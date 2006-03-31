package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class SharedDirectory {
	
	public static String SEPARATOR = "/";
	
	private File file;
	private String name;
	private ShareSettings settings;

	public SharedDirectory(File f, String n, ShareSettings s) {
		file = f;
		name = n;
		settings = s;
	}
	
	public List<SharedFile> getFiles() {
		LinkedList<SharedFile> l = new LinkedList<SharedFile>(); 
		for (File f: file.listFiles()) {
			if (f.isFile() && !f.isHidden() && f.canRead()) {
				l.add(new SharedFile(f, getPathToParent(), settings));
			}
		}
		return l;
	}
	
	public List<SharedDirectory> getDirectories() {
		LinkedList<SharedDirectory> l = new LinkedList<SharedDirectory>(); 
		for (File f: file.listFiles()) {
			if (f.isDirectory() && !f.isHidden()) {
				l.add(new SharedDirectory(f, name + SEPARATOR + f.getName(), settings));
			}
		}
		return l;
	}
	
	public SharedFile getFile(String name) throws FileNotFoundException {
		for (File f: file.listFiles()) {
			if (f.isFile() && !f.isHidden() && f.canRead() && f.getName().equals(name)) {
				return new SharedFile(f, getPathToParent(), settings);
			}
		}
		throw new FileNotFoundException(name);
	}
	
	public SharedDirectory getDirectory(String name) throws FileNotFoundException {
		for (File f: file.listFiles()) {
			if (f.isDirectory() && !f.isHidden() && f.getName().equals(name)) {
				return new SharedDirectory(f, name + SEPARATOR + f.getName(), settings);
			}
		}
		throw new FileNotFoundException(name);
	}
	
	public boolean containsDirectory(String name) {
		for (File f: file.listFiles()) {
			if (f.isDirectory() && !f.isHidden() && f.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsFile(String name) {
		for (File f: file.listFiles()) {
			if (f.isFile() && !f.isHidden() && f.canRead() && f.getName().equals(name)) {
				return true;
			}
		}
		return false;		
	}
	
	public List<SharedDirectory> getMatchingDirectories(String n) {
		LinkedList<SharedDirectory> ls = new LinkedList<SharedDirectory>();
		ls.addAll(getMatchingDirectories(file, n, name));
		return ls;
	}
	
	private List<SharedDirectory> getMatchingDirectories(File dir, String n, String path) {
		LinkedList<SharedDirectory> ls = new LinkedList<SharedDirectory>();
		for (File f: dir.listFiles()) {
			if (f.isDirectory() && !f.isHidden()) {
				if (f.getName().contains(n)) {
					ls.add(new SharedDirectory(f, path + SEPARATOR + f.getName(), settings));
				}
				ls.addAll(getMatchingDirectories(f, n, path + SEPARATOR + f.getName()));
			}
		}
		return ls;
	}
	
	public List<SharedFile> getMatchingFiles(String n) {
		LinkedList<SharedFile> ls = new LinkedList<SharedFile>();
		ls.addAll(getMatchingFiles(file, n, name));
		return ls;
	}
	
	private List<SharedFile> getMatchingFiles(File dir, String n, String path) {
		LinkedList<SharedFile> ls = new LinkedList<SharedFile>();
		for (File f: dir.listFiles()) {
			if (f.isFile() && !f.isHidden() && f.canRead()) {
				if (f.getName().contains(n)) {
					ls.add(new SharedFile(f, genPath(path), settings));
				}
			} else if (f.isDirectory() && !f.isHidden()) {
				ls.addAll(getMatchingFiles(f, n, path + SEPARATOR + f.getName()));
			}
		}
		return ls;
	}
	
	public SharedFile findFile(List<String> path, String name) throws FileNotFoundException {
		File dir = file;
		for (String s: path) {
			File newdir = null;
			for (File f: dir.listFiles()) {
				if (f.isDirectory() && !f.isHidden() && f.getName().equals(s)) {
					newdir = f;
				}
			}
			if (newdir == null) {
				throw new FileNotFoundException(s);
			} else {
				dir = newdir;
			}
		}
		// The path is now finished
		for (File f: dir.listFiles()) {
			if (f.isFile() && !f.isHidden() && f.canRead() && f.getName().equals(name)) {
				return new SharedFile(f, path, settings);
			}
		}
		throw new FileNotFoundException(name);
	}
	
	public SharedFile findFile(String path, String name) throws FileNotFoundException {
		LinkedList<String> l = new LinkedList<String>();
		if (path.length() > 0) {
			for (String s: path.split(SEPARATOR)) {
				l.add(s);
			}
		}
		return findFile(l, name);
	}
	
	public SharedDirectory findDirectory(List<String> path) throws FileNotFoundException {
		File dir = file;
		for (String s: path) {
			File newdir = null;
			for (File f: dir.listFiles()) {
				if (f.isDirectory() && !f.isHidden() && f.getName().equals(s)) {
					newdir = f;
				}
			}
			if (newdir == null) {
				throw new FileNotFoundException(s);
			} else {
				dir = newdir;
			}
		}
		return new SharedDirectory(dir, genPath(path), settings);
	}
	
	public SharedDirectory findDirectory(String path) throws FileNotFoundException {
		LinkedList<String> l = new LinkedList<String>();
		if (path.length() > 0) {
			for (String s: path.split(SEPARATOR)) {
				l.add(s);
			}
		}
		return findDirectory(l);
	}
	
	public String getName() {
		String[] s = name.split(SEPARATOR);
		return s[s.length - 1];
	}
	
	public String getPath() {
		return name;
	}
	
	public String getFilePath() {
		return file.getPath();
	}
	
	private String genPath(List<String> l) {
		String r = getPath();
		for (String s: l) {
			r += SEPARATOR + s;
		}
		return r;
	}
	
	private List<String> genPath(String n) {
		LinkedList<String> l = new LinkedList<String>();
		for (String s: n.split(SEPARATOR)) {
			l.add(s);
		}
		return l;
	}
	
	protected List<String> getPathToParent() {
		return genPath(name);
	}

	protected void hash() {
		hash(file);
	}
	
	private void hash(File fl) {
		for (File f: fl.listFiles()) {
			// Hash
			if (f.isDirectory() && !f.isHidden()) {
				// Hash children
				hash(f);
			} else if (f.isFile() && !f.isHidden() && f.canRead()) {
				if (settings.getAccessDate(f.getPath()) < f.lastModified()) {
					// Update hash
					byte[] h = SharedFile.hash(f);
					settings.setData(f.getPath(), h, f.lastModified());
				}
			}
		}
	}
}
