package edu.tue.compnet.events;

/**
 * Listens for certain questions, this is specifically usefull for user
 * interaction.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface QueryListener {	
	/**
	 * Give all listeners a message.
	 * @param e The event associated with this message.
	 */
	void notice(Event e);
	
	/**
	 * Ask all listeners something, if 1 agrees, it's fine
	 * @param e the event associated with this question.
	 * @param title The title of the screen that asks the question.
	 * @return The answer. 
	 */
	boolean ask(Event e, String title);
	
	/**
	 * Different options of an answer.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	public enum Answer {
		YES,
		NO,
		CANCEL;
	}
	
	/**
	 * Ask for a yes/no/cancel question.
	 * @param e The event that is associated with this.
	 * @param title The title of the screen that asks the question.
	 * @return The answer.
	 */
	Answer askForTristate(Event e, String title);
}
