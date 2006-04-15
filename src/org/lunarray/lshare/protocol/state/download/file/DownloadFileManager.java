package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.TreeMap;


public class DownloadFileManager {

	private TreeMap<File, IncompleteFile> files;
	
	public IncompleteFile getFile(File f) {
		if (files.containsKey(f)) {
			return files.get(f);
		} else {
			IncompleteFile i = new IncompleteFile(f);
			files.put(f, i);
			return i;
		}
	}	
}
