package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.filelist.FilelistEntry;
import org.lunarray.lshare.protocol.state.search.SearchList;

public class SearchEvent {

	private FilelistEntry entry;
	private SearchList source;
	
	public SearchEvent(FilelistEntry e, SearchList s) {
		entry = e;
		source = s;
	}
	
	public SearchList getSource() {
		return source;
	}
	
	public FilelistEntry getEntry() {
		return entry;
	}
}
