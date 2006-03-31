package org.lunarray.lshare.gui.main;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import org.lunarray.lshare.gui.MainGUI;

public class ShowFrameMenu extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -5480412008853738195L;
	private JInternalFrame frame;
	private JDesktopPane parent;
	
	public ShowFrameMenu(JInternalFrame f, JDesktopPane fr) {
		super(f.getTitle());
		frame = f;
		parent = fr;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		frame.moveToFront();
		try {
			frame.setIcon(false);
		} catch (PropertyVetoException pve) {
			// Tough luck
		}		
		Point p = frame.getLocation();
		if (p.x < 0 || p.x + frame.getWidth() > parent.getWidth() - MainGUI.FRAME_PAD) {
			p.x = 0;
		}
		if (p.y < 0 || p.y > parent.getHeight() - MainGUI.FRAME_PAD) {
			p.y = 0;
		}
		frame.setLocation(p);
	}
}
