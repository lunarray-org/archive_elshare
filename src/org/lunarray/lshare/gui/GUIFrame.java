package org.lunarray.lshare.gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.settings.GUISettings;

/**
 * An abstract class to provide some standard functionality for an internal
 * frame in the application.
 * @author Pal Hargitai
 */
public abstract class GUIFrame extends InternalFrameAdapter {
    /**
     * The internal frame that this class abstracts functionality for.
     */
    protected JInternalFrame frame;

    /**
     * The main user interface, to update in case of events.
     */
    private MainGUI main;

    /**
     * Usable settings
     */
    private GUISettings set;

    /**
     * Instanciates the frame with some default values.
     * @param mg The main GUI.
     * @param ls The controls to the protocol
     */
    public GUIFrame(MainGUI mg, LShare ls) {
        main = mg;
        set = ls.getSettings().getSettingsForGUI();

        frame = new JInternalFrame();
        // Set defaults
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setPreferredSize(new Dimension(320, 240));
        frame
                .setMaximumSize(new Dimension(Integer.MAX_VALUE,
                        Integer.MAX_VALUE));
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

        String l = "/" + getClass().getSimpleName();
        frame.setLocation(set.getInt(l, "x", 0), set.getInt(l, "y", 0));
        frame.setSize(set.getInt(l, "w", 640), set.getInt(l, "h", 480));
    }

    /**
     * Gets the frame that is set up.
     * @return The setup frame.
     */
    public JInternalFrame getFrame() {
        return frame;
    }

    /**
     * Should provide the actions on close. This may be to dispose the frame, or
     * just hide it.
     */
    public abstract void close();

    @Override
    /**
     * Gives some functionality required for closing this frame.
     * @param arg0 The frame event that triggered this call.
     */
    public void internalFrameDeactivated(InternalFrameEvent arg0) {
        internalFrameClosing(arg0);
    }

    @Override
    /**
     * Gives some functionality required for closing this frame.
     * @param arg0 The frame event that triggered this call.
     */
    public void internalFrameClosing(InternalFrameEvent arg0) {
        String l = "/" + getClass().getSimpleName();
        set.setInt(l, "x", frame.getX());
        set.setInt(l, "y", frame.getY());
        set.setInt(l, "w", frame.getWidth());
        set.setInt(l, "h", frame.getHeight());

        close();
        main.updateMenu();
    }
}
