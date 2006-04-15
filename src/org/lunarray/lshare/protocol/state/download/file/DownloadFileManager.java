package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;


public class DownloadFileManager {

	private TreeMap<File, IncompleteFile> files;
	
//	 TODO
	public DownloadFileManager() {
//		 TODO
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
			IncompleteFile n = new IncompleteFile(f);
			
			try {
				n.setSize(s);
				
				files.put(f, n);
				return n;
			} catch (InvalidFileStateException ifse) {
				// Shouldn't happen, but throw new exception
				throw new FileExistsException();
			}
		} else {
			throw new FileExistsException();
		}
	}
}
