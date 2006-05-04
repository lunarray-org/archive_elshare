package org.lunarray.lshare.gui.transfers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

/**
 * A transfer list.
 * @author Pal Hargitai
 */
public class TransferList extends GUIFrame implements ActionListener,
        ListSelectionListener {

    /**
     * The table showing the transfers.
     */
    private JTable table;

    /**
     * The model holding representations of the transfers.
     */
    private TransferModel model;

    /**
     * The timer that is to update the table.
     */
    private Timer timer;

    /**
     * The selected item.
     */
    private TransferItem selected;

    /**
     * The cancel button.
     */
    private JButton cancel;

    /**
     * Constructs a transferlist.
     * @param ls The controls to the protocol.
     * @param mg The main user interface.
     */
    public TransferList(LShare ls, MainGUI mg) {
        super(mg);

        model = new TransferModel();
        ls.getDownloadManager().addTransferListener(model);
        ls.getUploadManager().addListener(model);
        table = new JTable(model);
        table.setDefaultRenderer(JProgressBar.class, model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane(table);

        // Setup update timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateTable();
            }
        }, 0, 1000);

        // Setup toolbar
        JToolBar bar = new JToolBar();

        cancel = new JButton();
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        cancel.setEnabled(false);
        // cancel.setText("Cancel"); <- Use icon
        cancel.setIcon(new ImageIcon("icons/process-stop.png"));

        bar.add(cancel);

        // Setup main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // The frame
        frame.setTitle(getTitle());
        frame.add(mp);
    }

    /**
     * Triggered if a list selection has occured.
     * @param arg0 The event associated with the selection.
     */
    public void valueChanged(ListSelectionEvent arg0) {
        selected = arg0.getFirstIndex() >= 0
                && arg0.getFirstIndex() < model.getRowCount() ? model
                .getRow(arg0.getFirstIndex()) : null;
        setButtonsEnabled(selected != null);
    }

    /**
     * Triggered if an action has been performed.
     * @param arg0 The event associated with the action.
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("cancel")) {
            if (selected != null) {
                selected.cancel();
            }
        }
    }

    @Override
    /**
     * Hides the frame.
     */
    public void close() {
        frame.setVisible(false);
        timer.cancel();
    }

    /**
     * Gets the title of the frame.
     * @return The title of the frame.
     */
    public String getTitle() {
        return "Transfers";
    }

    /**
     * Enables or disables all the buttons.
     * @param enabled True if they should be enabled, false if not.
     */
    private void setButtonsEnabled(boolean enabled) {
        cancel.setEnabled(enabled);
    }
}
