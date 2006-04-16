package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.state.download.DownloadSettings;


public class DownloadFileManager {

	private TreeMap<File, IncompleteFile> files;
	private DownloadSettings settings;
	private Controls controls;
	
	public DownloadFileManager(Controls c) {
		controls = c;
		settings = controls.getSettings().getDownloadSettings();
		
		init();
	}
	
	private void init() {
		for (String s: settings.getFileKeys()) {
			IncompleteFileSettings e = new IncompleteFileSettings(s, settings);
			IncompleteFile n = new IncompleteFile(e, controls);
			
			files.put(e.getLocalTarget(), n);
		}
	}
	
	public IncompleteFile getFile(File f) throws FileNotFoundException {
		if (files.containsKey(f)) {
			return files.get(f);
		} else {
			throw new FileNotFoundException();
		}
	}
	
	public boolean fileExists(File f) {
		return files.containsKey(f);
	}
	
	public IncompleteFile newFile(File f, long s) throws FileExistsException {
		if (!files.containsKey(f) && !f.exists()) {
			Hash fnh = new Hash(f.getPath());
			
			IncompleteFileSettings e = new IncompleteFileSettings(fnh.toString(), settings);
			
			IncompleteFile n = new IncompleteFile(e, controls);
			
			try {
				n.setSize(s);
				
				files.put(f, n);
				return n;
			} catch (IllegalStateException ifse) {
				// Shouldn't happen, but throw new exception
				throw new FileExistsException();
			}
		} else {
			throw new FileExistsException();
		}
	}
}
