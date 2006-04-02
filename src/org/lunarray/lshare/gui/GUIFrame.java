package org.lunarray.lshare.gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public abstract class GUIFrame extends InternalFrameAdapter {
	
	protected JInternalFrame frame;
	
	public GUIFrame() {
		frame = new JInternalFrame();

		frame.setMinimumSize(new Dimension(320, 240));
		frame.setPreferredSize(new Dimension(320, 240));
		frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		frame.pack();
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setClosable(true);
		frame.setMaximizable(true);
		frame.setIconifiable(true);
	}

	public JInternalFrame getFrame() {
		return frame;
	}
	
	public void close() {}
	
	public void internalFrameClosing(InternalFrameEvent arg0) {
		close();
	}
}
