package org.lunarray.lshare.gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

public abstract class GUIFrame {
	
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
}
