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
package org.lunarray.lshare.gui.incomplete;

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
 * TODO Add Search for additional sources<br>
 * Shows a list of incomplete files.
 * @author Pal Hargitai
 */
public class IncompleteList extends GUIFrame implements ActionListener,
        ListSelectionListener {

    /**
     * A timer to update these files.
     */
    private Timer timer;

    /**
     * The model in which representations of this file will be stored.
     */
    private IncompleteModel model;

    /**
     * The selected row. This is < 0 if nothing is selected, it will be >= 0 if
     * there is a selection.
     */
    private int selrow;

    /**
     * A delete button.
     */
    private JButton delete;

    /**
     * Usable settings.
     */
    private GUISettings set;

    /**
     * The table
     */
    private JTable table;

    /**
     * Constructs the incomplete filelist
     * @param ls The controls of the protocol.
     * @param mg The main user interface.
     */
    public IncompleteList(LShare ls, MainGUI mg) {
        super(mg, ls);
        set = ls.getSettings().getSettingsForGUI();

        // Setup table
        model = new IncompleteModel(ls);
        ls.getDownloadManager().addQueueListener(model);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
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

        // Setup toolbar
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        delete = new JButton();
        delete.setActionCommand("delete");
        delete.addActionListener(this);
        delete.setEnabled(false);
        // delete.setText("Delete"); <- Use icon
        delete.setIcon(TangoFactory.getIcon("edit-delete"));

        bar.add(delete);

        // Setup main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // Set frame
        frame.setTitle(getTitle());
        frame.setContentPane(mp);
        frame.setFrameIcon(TangoFactory.getIcon("text-x-generic"));

        // Setup update timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateTable();
            }
        }, 0, 1000);
    }

    /**
     * Triggered if a list selection occurs.
     * @param arg0 The event associated with the selection.
     */
    public void valueChanged(ListSelectionEvent arg0) {
        selrow = arg0.getFirstIndex() >= 0
                && arg0.getFirstIndex() < model.getRowCount() ? arg0
                .getFirstIndex() : -1;
        setButtonsEnabled(selrow >= 0);
    }

    /**
     * Triggered if an action is performed.
     * @param arg0 The event associated with the action.
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("delete")) {
            if (selrow >= 0) {
                model.getRow(selrow).delete();
                model.deleteRow(selrow);
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
        return "Incomplete";
    }

    /**
     * Enables or disabled the buttons.
     * @param enabled The buttons will be enabled.
     */
    private void setButtonsEnabled(boolean enabled) {
        delete.setEnabled(enabled);
    }
}
