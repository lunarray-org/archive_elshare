package org.lunarray.lshare.gui.search;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;

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
     * The selected rows.
     */
    private ArrayList<SearchEvent> selected;

    /**
     * Wether the next select event is the new in a series.
     */
    private boolean isnewevent;

    /**
     * The table with the results.
     */
    private JTable restable;

    /**
     * Constructs a search frame.
     * @param ls The instance of the protocol to assocaite with.
     * @param f The filter that is to be used.
     * @param mg The main user interface that this frame resides in.
     */
    public SearchList(LShare ls, SearchFilter f, MainGUI mg) {
        super(mg);

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

        // Set the toolbar
        JToolBar bar = new JToolBar();
        download = new JButton();
        download.setActionCommand("download");
        download.addActionListener(this);
        download.setEnabled(false);
        // download.setText("Download"); <- Use icon
        download.setIcon(new ImageIcon("icons/document-save.png"));
        bar.add(download);
        downloadto = new JButton();
        downloadto.setActionCommand("downloadto");
        downloadto.addActionListener(this);
        downloadto.setEnabled(false);
        // downloadto.setText("Download To"); <- Use icon
        downloadto.setIcon(new ImageIcon("icons/document-save-as.png"));
        bar.add(downloadto);

        // Set the main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // Setup frame
        frame.setTitle(getTitle());
        frame.getContentPane().add(mp);

        isnewevent = true;
        selected = new ArrayList<SearchEvent>();
    }

    /**
     * Triggered if the selection of the list changes.
     * @param arg0 The event associatd with the selection.
     */
    public void valueChanged(ListSelectionEvent arg0) {
        // TODO test thoroughly and make it behave properly!
        isnewevent = !restable.getSelectionModel().getValueIsAdjusting();
        if (isnewevent) {
            selected.clear();
        }
        if (arg0.getFirstIndex() >= 0
                && arg0.getLastIndex() < model.getRowCount()) {
            for (int i = arg0.getFirstIndex(); i < arg0.getLastIndex(); i++) {
                if (!selected.contains(model.getRow(i))) {
                    selected.add(model.getRow(i));
                }
            }
        }
        setButtonsEnabled(!selected.isEmpty());
    }

    /**
     * Triggered if an action is performed.
     * @param arg0 The event assocated with the action.
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("download")) {
            
            for (SearchEvent e : selected) {
                lshare.getDownloadManager().enqueue(e.getEntry(), e.getUser());
            }
        } else if (arg0.getActionCommand().equals("downloadto")) {
            
            JFileChooser fc = new JFileChooser();
            if (selected.size() == 1 && selected.get(0).getEntry().isFile()) {
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            } else {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            
            int res = fc.showDialog(frame, "Download to...");
            if (res == JFileChooser.APPROVE_OPTION) {
                for (SearchEvent e : selected) {
                    lshare.getDownloadManager().enqueue(e.getEntry(),
                            e.getUser(), fc.getSelectedFile());
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
