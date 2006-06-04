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
package org.lunarray.lshare.gui.search;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;
import org.lunarray.lshare.protocol.settings.GUISettings;

/**
 * A frame for showing search results in.
 * @author Pal Hargitai
 */
public class SearchList extends GUIFrame implements SearchListener,
        ActionListener, ListSelectionListener {
    /**
     * The filter that represents the kind of search going on.
     */
    private SearchFilter filter;

    /**
     * The instance of the protocol that this panel is to listen to.
     */
    private LShare lshare;

    /**
     * The model that represents this frames search results.
     */
    private SearchModel model;

    /**
     * The download button.
     */
    private JButton download;

    /**
     * The download to.. button.
     */
    private JButton downloadto;

    /**
     * The table with the results.
     */
    private JTable restable;

    private GUISettings set;

    /**
     * Constructs a search frame.
     * @param ls The instance of the protocol to assocaite with.
     * @param f The filter that is to be used.
     * @param mg The main user interface that this frame resides in.
     */
    public SearchList(LShare ls, SearchFilter f, MainGUI mg) {
        super(mg, ls);
        set = ls.getSettings().getSettingsForGUI();

        // Setup protocol
        filter = f;
        lshare = ls;
        lshare.getSearchList().addListener(this);

        // Setup model
        model = new SearchModel();
        restable = new JTable(model);
        model.setTableHeader(restable.getTableHeader());
        JScrollPane sp = new JScrollPane(restable);
        restable.getSelectionModel().addListSelectionListener(this);
        restable.getSelectionModel().setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        restable.getTableHeader().setReorderingAllowed(false);
        restable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < model.getColumnCount(); i++) {
            String k = "c" + i + "w";
            int w = set.getInt("/" + getClass().getSimpleName(), k, -1);
            if (w >= 0) {
                restable.getColumnModel().getColumn(i).setPreferredWidth(w);
            }
        }

        // Set the toolbar
        JToolBar bar = new JToolBar();
        download = new JButton();
        download.setActionCommand("download");
        download.addActionListener(this);
        download.setEnabled(false);
        // download.setText("Download"); <- Use icon
        download.setIcon(new ImageIcon(ClassLoader
                .getSystemResource("icons/document-save.png")));
        bar.add(download);
        downloadto = new JButton();
        downloadto.setActionCommand("downloadto");
        downloadto.addActionListener(this);
        downloadto.setEnabled(false);
        // downloadto.setText("Download To"); <- Use icon
        downloadto.setIcon(new ImageIcon(ClassLoader
                .getSystemResource("icons/document-save-as.png")));
        bar.add(downloadto);

        // Set the main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // Setup frame
        frame.setTitle(getTitle());
        frame.getContentPane().add(mp);
    }

    /**
     * Triggered if the selection of the list changes.
     * @param arg0 The event associatd with the selection.
     */
    public void valueChanged(ListSelectionEvent arg0) {
        setButtonsEnabled(restable.getSelectedRowCount() != 0);
    }

    /**
     * Triggered if an action is performed.
     * @param arg0 The event assocated with the action.
     */
    public void actionPerformed(ActionEvent arg0) {
        for (int i : restable.getSelectedRows()) {
            System.out.println(model.getRow(i).getEntry());
        }
        if (arg0.getActionCommand().equals("download")) {
            for (int i : restable.getSelectedRows()) {
                lshare.getDownloadManager().enqueue(model.getRow(i).getEntry(),
                        model.getRow(i).getUser());
            }
        } else if (arg0.getActionCommand().equals("downloadto")) {

            JFileChooser fc = new JFileChooser();
            if (restable.getSelectedRowCount() == 1
                    && model.getRow(restable.getSelectedRow()).getEntry()
                            .isFile()) {
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            } else {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }

            int res = fc.showDialog(frame, "Download to...");
            if (res == JFileChooser.APPROVE_OPTION) {
                for (int i : restable.getSelectedRows()) {
                    lshare.getDownloadManager().enqueue(
                            model.getRow(i).getEntry(),
                            model.getRow(i).getUser(), fc.getSelectedFile());
                }
            }
        }
    }

    /**
     * Gets the title for this frame.
     * @return The title of this frame, generally "Search:" appended by the
     * filter name.
     */
    public String getTitle() {
        return "Search:" + filter.getName();
    }

    /**
     * Called when a search result comes in.
     * @param e The event that came with this call.
     */
    public void searchResult(SearchEvent e) {
        if (filter.isValid(e)) {
            model.processEvent(e);
        }
    }

    @Override
    /**
     * Closes this frame and cleans it up.
     */
    public void close() {
        for (int i = 0; i < model.getColumnCount(); i++) {
            set.setInt("/" + getClass().getSimpleName(), "c" + i + "w",
                    restable.getColumnModel().getColumn(i).getWidth());
        }

        lshare.getSearchList().removeListener(this);
        frame.dispose();
    }

    /**
     * Disable or enable the buttons.
     * @param enabled Set to true if the buttons should be enabled, false if
     * not.
     */
    private void setButtonsEnabled(boolean enabled) {
        download.setEnabled(enabled);
        downloadto.setEnabled(enabled);
    }
}
