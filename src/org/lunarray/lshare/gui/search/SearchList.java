package org.lunarray.lshare.gui.search;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;

/**
 * A frame for showing search results in.
 * @author Pal Hargitai
 */
public class SearchList extends GUIFrame implements SearchListener {
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
        JTable restable = new JTable(model);
        JScrollPane sp = new JScrollPane(restable);

        // Setup
        frame.addInternalFrameListener(this);
        frame.add(sp);
        frame.setTitle(getTitle());
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
}
