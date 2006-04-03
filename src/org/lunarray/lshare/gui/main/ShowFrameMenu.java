package org.lunarray.lshare.gui.main;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import org.lunarray.lshare.gui.MainGUI;

/**
 * A class to provide the functionality of 'popping' a window to the front.
 * @author Pal Hargitai
 */
public class ShowFrameMenu extends JMenuItem implements ActionListener {

	/**
	 * The serial ID.
	 */
	private static final long serialVersionUID = -5480412008853738195L;
	
	/**
	 * The internal frame to show when this is clicked.
	 */
	private JInternalFrame frame;
	
	/**
	 * The desktop pane where the frame associated with this menuitem.
	 */
	private JDesktopPane parent;
	
	/**
	 * Constructs the menu item.
	 * @param f The frame to show if this item is clicked.
	 * @param fr The desktop pane this frame is contained in.
	 */
	public ShowFrameMenu(JInternalFrame f, JDesktopPane fr) {
		super(f.getTitle());
		frame = f;
		parent = fr;
		addActionListener(this);
	}

	/**
	 * The action performed when this item is clicked on.
	 * @param arg0 The action associated with the calling of this function.
	 */
	public void actionPerformed(ActionEvent arg0) {
		// Move to front
		frame.moveToFront();
		
		// Unhide
		try {
			frame.setIcon(false);
		} catch (PropertyVetoException pve) {
			// Tough luck
		}
		
		// Show in the right place.
		Point p = frame.getLocation();
		if (p.x < 0 || p.x + frame.getWidth() > parent.getWidth() - MainGUI.
				FRAME_PAD) {
			p.x = 0;
		}
		if (p.y < 0 || p.y > parent.getHeight() - MainGUI.FRAME_PAD) {
			p.y = 0;
		}
		frame.setLocation(p);
	}
}
