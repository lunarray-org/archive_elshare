package edu.tue.compnet.protocol.state;

import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.State;

/**
 * This class handles things related to the query listener. The different
 * classes should use this class to ask questions to the gui.s 
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Query {
	// The listeners
	QueryListener querylisteners;
	// The state
	State state;
	
	/**
	 * Constructs the query.
	 * @param s The state this query is associated with.
	 */
	public Query(State s) {
		state = s;
	}
	
	/**
	 * Add a listener to notify when events happen.
	 * @param lis The listener to add.
	 */
	public void addQueryListener(QueryListener lis) {
		querylisteners = lis;
	}
	
	/**
	 * Ask all listeners a question, if any of them say yes, it's yes.
	 * @param question The question asked.
	 * @param title The title of the question. For a prompt.
	 * @return If anybody agrees, true, else false.
	 */
	public synchronized boolean askListeners(String question, String title) {
		if (querylisteners != null) {
			Event ev = new Event(state, question);
			return querylisteners.ask(ev, title);
		} else {
			return false;
		}
	}
	
	/**
	 * Ask wether an upload should be permitted.
	 * @param question Ask for upload.
	 * @param title The title of the question. For a prompt.
	 * @return The answer.
	 */
	public synchronized QueryListener.Answer askListenersForUpload(String
			question, String title) {
		if (querylisteners != null) {
			Event ev = new Event(state, question);
			return querylisteners.askForTristate(ev, title);
		} else {
			return QueryListener.Answer.CANCEL;
		}
	}
	
	/**
	 * Notify all listeners with a message.
	 * @param message The message to notify with.
	 */
	public synchronized void notifyListeners(String message) {
		if (querylisteners != null) {
			Event ev = new Event(state, message);
			querylisteners.notice(ev);
		}
	}
}
