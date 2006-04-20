package org.lunarray.lshare.gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/** An abstract class to provide some standard functionality for an internal
 * frame in the application.
 * @author Pal Hargitai
 */
public abstract class GUIFrame extends InternalFrameAdapter {
	/** The internal frame that this class abstracts functionality for.
	 */
	protected JInternalFrame frame;
	
	/** The main user interface, to update in case of events.
	 */
	private MainGUI main;
	
	/** Instanciates the frame with some default values.
	 * @param mg The main GUI.
	 */
	public GUIFrame(MainGUI mg) {
		main = mg;
		frame = new JInternalFrame();
		// Set defaults
		frame.setMinimumSize(new Dimension(320, 240));
		frame.setPreferredSize(new Dimension(320, 240));
		frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.
				MAX_VALUE));
		frame.pack();
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setClosable(true);
		frame.setMaximizable(true);
		frame.setIconifiable(true);
		// Set close functions
		frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		frame.addInternalFrameListener(this);
	}

	/** Gets the frame that is set up.
	 * @return The setup frame.
	 */
	public JInternalFrame getFrame() {
		return frame;
	}
	
	/** Should provide the actions on close.
	 * This may be to dispose the frame, or just hide it.
	 */
	public abstract void close();
	
	@Override
	/** Gives some functionality required for closing this frame.
	 * @param arg0 The frame event that triggered this call.
	 */
	public void internalFrameClosing(InternalFrameEvent arg0) {
		close();
		main.updateMenu();
	}
}
