package org.lunarray.lshare.protocol.state.download.file;

import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

public class IncompleteFile {

	private Hash hash;
	private ChunkedFile file;
	private IncompleteFileSettings settings;
	private Controls controls;
	private TreeMap<User, RemoteFile> sources;
	
	public IncompleteFile(IncompleteFileSettings s, Controls c) {
		settings = s;
		controls = c;
		sources = new TreeMap<User, RemoteFile>();
		file = new ChunkedFile(settings);
	}
	
	protected void addSource(User u, RemoteFile f) throws IllegalArgumentException {
		if (f.getSize() == getSize()) {
			if (hash.isEmpty()) {
				hash = f.getHash();
				
				sources.put(u, f);
			} else {
				if (hash.equals(f.getHash())) {
					sources.put(u, f);
				} else {
					throw new IllegalArgumentException();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected void removeSource(User u) {
		if (sources.containsKey(u)) {
			sources.remove(u);
		}
	}
	
	protected void initFromSettings() {
		file.initFromBack();
		hash = settings.getHash();
		
		for (String uc: settings.getSources()) {
			try {
				User u = controls.getState().getUserList().
						findUserByChallenge(uc);
				
				String path = settings.getSourcePath(uc);
				String name = settings.getSourceName(uc);
				
				QueuedRemote r = new QueuedRemote(path, name, hash, file.getSize());
				
				sources.put(u, r);
			} catch (UserNotFound unf) {
				// Ignore
			}
		}
	}
	
	protected void close() {
		// Close file
		file.close();
		
		settings.setHash(hash);
		
		for (User u: sources.keySet()) {
			RemoteFile f = sources.get(u);
			
			if (u.isBuddy()) {
				settings.setSource(u.getChallenge(), f.getPath(), f.getName());
			}
		}
	}
	
	public Chunk getChunk() throws IllegalAccessException {
		return file.getChunk();
	}
	
	public boolean matches(RemoteFile f) {
		if (file.getSize() > 0 && f.getSize() != file.getSize()) {
			return false;
		}
		if (hash.isEmpty()) {
			return true;
		} else {
			return hash.equals(f.getHash());
		}
	}
	
	protected void setHash(Hash h) throws IllegalArgumentException {
		if (hash.isEmpty()) {
			hash = h;
		} else {
			if (hash.equals(h)) {
				// do nothing
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
	
	protected void setSize(long s) throws IllegalStateException {
		file.setSize(s);
	}
	
	public long getSize() {
		return file.getSize();
	}
	
	public long getTodo() {
		return file.getTodo();
	}
	
	public long getDone() {
		return file.getDone();
	}
}
