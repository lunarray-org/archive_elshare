package edu.tue.compnet.protocol;

import java.util.Timer;
import java.util.TimerTask;

import edu.tue.compnet.protocol.state.*;

/**
 * This class keeps the state of the protocol, that is, user lists etc.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class State {
	// The settings
	Settings settings;
	// The settings of the state
	StateSettings statesettings;
	// The user list
	UserList userlist;
	// The file transfers
	FiletransferList ftlist;
	// The packet timeouts
	PacketTimeoutList ptlist;
	// The requests
	RequestList rqlist;
	// The buddy list
	BuddyList blist;
	// The query
	Query query;
	// The search
	SearchList slist;
	// The allow list
	AllowList alist;
	// The transferfile list
	TransferFileList tflist;
	// General tasks
	Tasks tasks;
	// The userlist request list
	UserlistRequestList urqlist;
	// The hash list
	HashList hlist;
	// The task timer
	Timer timedtasks;
	
	/**
	 * The constructor for the protocol state.
	 */
	public State() {
		timedtasks = new Timer();
		ptlist = new PacketTimeoutList();
		rqlist = new RequestList();
		settings = new Settings();
		alist = new AllowList();
		urqlist = new UserlistRequestList();
		statesettings = new StateSettings(settings);
		blist = new BuddyList(settings);
		ftlist = new FiletransferList(this);
		userlist = new UserList(this);
		query = new Query(this);
		slist = new SearchList(this);
		tasks = new Tasks(this);
		tflist = new TransferFileList(settings, this);
		hlist = new HashList(settings, this);
	}
	
	/**
	 * Stops the state, essentially stops tasks
	 */
	public void quit() {
		timedtasks.cancel();
	}
	
	/**
	 * Gets the hash list
	 * @return The hash list
	 */
	public HashList getHashList() {
		return hlist;
	}
	
	/**
	 * Gets the user list request list.
	 * @return The list.
	 */
	public UserlistRequestList getUserlistRequestList() {
		return urqlist;
	}
	
	/**
	 * Gets the general taks
	 * @return Get tasks.
	 */
	public Tasks getTasks() {
		return tasks;
	}
	
	/**
	 * Gets the settings of the protocol.
	 * @return The settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Get the packettimeout list
	 * @return The list.
	 */
	public PacketTimeoutList getPacketTimeoutList() {
		return ptlist;
	}
	
	/**
	 * Get the request list.
	 * @return The list.
	 */
	public RequestList getRequestList() {
		return rqlist;
	}
	
	/**
	 * Get the buddy list.
	 * @return The list.
	 */
	public BuddyList getBuddyList() {
		return blist;
	}
	
	/**
	 * Get the filetransfer list.
	 * @return The list.
	 */
	public FiletransferList getFiletransferList() {
		return ftlist;
	}

	/**
	 * Get the user list.
	 * @return The user list.
	 */
	public UserList getUserList() {
		return userlist;
	}
	
	/**
	 * Get the state settings
	 * @return The settings.
	 */
	public StateSettings getStateSettings() {
		return statesettings;
	}
	
	/**
	 * Get the search listeners.
	 * @return The listeners.
	 */
	public SearchList getSearchList() {
		return slist;
	}
	
	/**
	 * Get the query object.
	 * @return The object that handles questions.
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * Get the list of allowed uploads.
	 * @return The list.
	 */
	public AllowList getAllowList() {
		return alist;
	}
	
	/**
	 * Gets the transfer file list.
	 * @return The list.
	 */
	public TransferFileList getTransferFileList() {
		return tflist;
	}
	
	
	/**
	 * Adds a timed task to be regularly executed.
	 * @param task The task to be executed.
	 * @param recurrence How often it should be executed.
	 */
	public void addProcotolTask(TimerTask task, int recurrence) {
		timedtasks.scheduleAtFixedRate(task, 0, recurrence);
	}
}