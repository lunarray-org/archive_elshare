package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.search.SearchList;
import org.lunarray.lshare.protocol.state.sharing.ShareList;
import org.lunarray.lshare.protocol.state.upload.UploadManager;
import org.lunarray.lshare.protocol.state.userlist.UserList;

/**
 * Represents the state of the protocol.
 * @author Pal Hargitai
 */
public class State {
	
	/**
	 * The user manager.
	 */
	private UserList ulist;
	
	/**
	 * The share manager.
	 */
	private ShareList slist;
	
	/**
	 * The search manager.
	 */
	private SearchList elist;
	
	private DownloadManager dman;
	private UploadManager uman;
	
	/**
	 * Initialises the state.
	 * @param c The controlls to provide access to the protocol.
	 */
	public void init(Controls c) {
		ulist = new UserList(c);
		slist = new ShareList(c);
		elist = new SearchList(c);
		dman = new DownloadManager(c);
		uman = new UploadManager(c);
	}
	
	/**
	 * Commits settings to persistent storage.
	 */
	public void commit() {
		// Will be used later for download management
		dman.close();
		uman.close();
	}
	
	/**
	 * Gets the user manager.
	 * @return The user manager.
	 */
	public UserList getUserList() {
		return ulist;
	}
	
	/**
	 * Gets the share manager.
	 * @return The share manager.
	 */
	public ShareList getShareList() {
		return slist;
	}
	
	/**
	 * Gets the search manager.
	 * @return The search manager.
	 */
	public SearchList getSearchList() {
		return elist;
	}
	
	public DownloadManager getDownloadManager() {
		return dman;
	}
	
	public UploadManager getUploadManager() {
		return uman;
	}
}
