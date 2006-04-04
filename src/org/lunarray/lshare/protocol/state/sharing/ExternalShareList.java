package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface ExternalShareList {
	public ShareEntry getEntryFor(File f) throws FileNotFoundException;
	
	public List<ShareEntry> getEntriesMatching(String s);
	
	public List<ShareEntry> getChildrenIn(String path) throws FileNotFoundException;
	
	public List<ShareEntry> getBaseEntries();
	
	public void removeShare(String sname);
	
	public void addShare(String sname, File fpath);
}
