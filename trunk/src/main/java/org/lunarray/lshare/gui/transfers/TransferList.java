/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.gui.transfers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

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
import org.lunarray.lshare.gui.TangoFactory;
import org.lunarray.lshare.protocol.settings.GUISettings;

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
     * Usable settings.
     */
    private GUISettings set;

    /**
     * Constructs a transferlist.
     * @param ls The controls to the protocol.
     * @param mg The main user interface.
     */
    public TransferList(LShare ls, MainGUI mg) {
        super(mg, ls);
        set = ls.getSettings().getSettingsForGUI();

        model = new TransferModel();
        ls.getDownloadManager().addTransferListener(model);
        ls.getUploadManager().addListener(model);

        table = new JTable(model);
        table.setDefaultRenderer(JProgressBar.class, model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);

        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < model.getColumnCount(); i++) {
            String k = "c" + i + "w";
            int w = set.getInt("/" + getClass().getSimpleName(), k, -1);
            if (w >= 0) {
                table.getColumnModel().getColumn(i).setPreferredWidth(w);
            }
        }        
        
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
        cancel.setIcon(TangoFactory.getIcon("process-stop"));

        bar.add(cancel);

        // Setup main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // The frame
        frame.setTitle(getTitle());
        frame.add(mp);
        frame.setFrameIcon(TangoFactory.getIcon("network-transmit-receive"));
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
        for (int i = 0; i < model.getColumnCount(); i++) {
            set.setInt("/" + getClass().getSimpleName(), "c" + i + "w", table
                    .getColumnModel().getColumn(i).getWidth());
        }

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
