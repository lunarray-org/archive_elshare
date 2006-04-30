package org.lunarray.lshare.gui.search;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.events.SearchEvent;

/**
 * A search model that contains and allows displaying of search results.
 * @author Pal Hargitai
 */
public class SearchModel implements TableModel, MouseListener,
        Comparator<SearchEvent> {
    /**
     * The events that have been processed, seen as search results.
     */
    private ArrayList<SearchEvent> events;

    /**
     * The listeners to this model.
     */
    private ArrayList<TableModelListener> listeners;

    private JTableHeader header;

    private int sortcolumn;

    private Sorting sortdirection;

    /**
     * Constructs the search model.
     */
    public SearchModel() {
        events = new ArrayList<SearchEvent>();
        listeners = new ArrayList<TableModelListener>();
        sortdirection = Sorting.NOT_SORTED;
        sortcolumn = 0;
    }

    public void setTableHeader(JTableHeader h) {
        if (header != null) {
            header.removeMouseListener(this);
        }
        header = h;
        header.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent arg0) {
        TableColumnModel cm = header.getColumnModel();
        int viewcol = cm.getColumnIndexAtX(arg0.getX());
        int actualcol = cm.getColumn(viewcol).getModelIndex();
        if (actualcol >= 0 && actualcol < 8) {
            sortcolumn = actualcol;
            switch (sortdirection) {
            case ASCENDING:
                sortdirection = Sorting.NOT_SORTED;
                break;
            case DESCENDING:
                sortdirection = Sorting.ASCENDING;
                break;
            case NOT_SORTED:
                sortdirection = Sorting.DESCENDING;
                break;
            default:
                // Nothing assigned, shound't happen, but assing not sorted
                sortdirection = Sorting.NOT_SORTED;
            }
            Collections.sort(events, this);
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        // Ignore
    }

    public void mouseExited(MouseEvent arg0) {
        // Ignore
    }

    public void mousePressed(MouseEvent arg0) {
        // Ignore
    }

    public void mouseReleased(MouseEvent arg0) {
        // Ignore
    }

    /**
     * Processes a search event.
     * @param e The search event to process.
     */
    public synchronized void processEvent(SearchEvent e) {
        search: {
            for (SearchEvent ev : events) {
                if (e.getEntry().getName().equals(ev.getEntry().getName())
                        && e.getEntry().getPath().equals(
                                ev.getEntry().getPath())
                        && e.getUser().getAddress().equals(
                                ev.getUser().getAddress())) {
                    break search;
                }
            }

            int i = Collections.binarySearch(events, e, this);
            if (i < 0) {
                events.add(-(i + 1), e);
            } else {
                events.add(i, e);
            }

            for (TableModelListener l : listeners) {
                TableModelEvent me = new TableModelEvent(this);
                l.tableChanged(me);
            }
        }
    }

    /**
     * Gets the amount of rows in this model.
     * @return The amount of rows.
     */
    public int getRowCount() {
        return events.size();
    }

    /**
     * Gets the amount of columns for displaying result data.
     * @return The amount of columns.
     */
    public int getColumnCount() {
        return 8;
    }

    /**
     * Gets the name of the specified column.
     * @param arg0 The index of the column.
     * @return The name of the column.
     */
    public String getColumnName(int arg0) {
        switch (arg0) {
        case 1:
            return "File name";
        case 2:
            return "File path";
        case 3:
            return "File size";
        case 4:
            return "Last modified";
        case 5:
            return "File hash";
        case 6:
            return "User name";
        case 7:
            return "User host";
        default:
            return "";
        }
    }

    /**
     * Gets the class of a column for rendering.
     * @param arg0 The column to check.
     * @return The class of the specified column.
     */
    public Class<?> getColumnClass(int arg0) {
        if (arg0 == 0) {
            return Icon.class;
        } else {
            return String.class;
        }
    }

    /**
     * Checks if a cell is editable.
     * @param arg0 The column of the cell.
     * @param arg1 The row of the cell.
     * @return By default false as no cell is editable.
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /**
     * Gets the value of a specified cell.
     * @param arg1 The row of the cell.
     * @param arg0 The column of the cell.
     * @return The value of the specified cell.
     */
    public Object getValueAt(int arg1, int arg0) {
        switch (arg0) {
        case 0:
            if (events.get(arg1).getEntry().isDirectory()) {
                return MetalIconFactory.getTreeFolderIcon();
            } else {
                return MetalIconFactory.getTreeLeafIcon();
            }
        case 1:
            return events.get(arg1).getEntry().getName();
        case 2:
            if (events.get(arg1).getEntry().isDirectory()) {
                return GUIUtil.stripDirPath(events.get(arg1).getEntry()
                        .getPath());
            } else {
                return events.get(arg1).getEntry().getPath();
            }
        case 3:
            if (events.get(arg1).getEntry().getSize() >= 0) {
                return GUIUtil.prettyPrint(events.get(arg1).getEntry()
                        .getSize());
            } else {
                return "";
            }
        case 4:
            if (events.get(arg1).getEntry().getLastModified() == 0) {
                return "";
            } else {
                return (new Date(events.get(arg1).getEntry().getLastModified()))
                        .toString();
            }
        case 5:
            if (events.get(arg1).getEntry().hasHash()) {
                return events.get(arg1).getEntry().getHash().toString();
            } else {
                return "";
            }

        case 6:
            return events.get(arg1).getUser().getName();
        case 7:
            return events.get(arg1).getUser().getHostname();
        default:
            return "";
        }
    }

    /**
     * Gets the value of a specified cell. Generally ignored.
     * @param arg0 The value to set the cell to.
     * @param arg1 The row of the cell.
     * @param arg2 The column of the cell.
     */
    public void setValueAt(Object arg0, int arg1, int arg2) {
        // Ignore
    }

    /**
     * Adds a listener to this table model.
     * @param arg0 The listener to add.
     */
    public void addTableModelListener(TableModelListener arg0) {
        listeners.add(arg0);
    }

    /**
     * Removes a listener from this model.
     * @param arg0 The listener to remove.
     */
    public void removeTableModelListener(TableModelListener arg0) {
        listeners.remove(arg0);
    }

    public int compare(SearchEvent arg0, SearchEvent arg1) {
        switch (sortdirection) {
        case ASCENDING:
            switch (sortcolumn) {
            case 1:
                return arg0.getEntry().getName().compareTo(
                        arg1.getEntry().getName());
            case 2:
                return arg0.getEntry().getPath().compareTo(
                        arg1.getEntry().getPath());
            case 3:
                return Long.valueOf(arg1.getEntry().getSize()).compareTo(
                        arg0.getEntry().getSize());
            case 4:
                return Long.valueOf(arg1.getEntry().getLastModified())
                        .compareTo(arg0.getEntry().getLastModified());
            case 5:
                return arg0.getEntry().getHash().compareTo(
                        arg1.getEntry().getHash());
            case 6:
                return arg0.getUser().compareTo(arg1.getUser());
            case 7:
                return arg0.getUser().getHostname().compareTo(
                        arg1.getUser().getHostname());
            default:
                return 0;
            }
        case DESCENDING:
            switch (sortcolumn) {
            case 1:
                return - arg0.getEntry().getName().compareTo(
                        arg1.getEntry().getName());
            case 2:
                return - arg0.getEntry().getPath().compareTo(
                        arg1.getEntry().getPath());
            case 3:
                return - Long.valueOf(arg1.getEntry().getSize()).compareTo(
                        arg0.getEntry().getSize());
            case 4:
                return - Long.valueOf(arg1.getEntry().getLastModified())
                        .compareTo(arg0.getEntry().getLastModified());
            case 5:
                return - arg0.getEntry().getHash().compareTo(
                        arg1.getEntry().getHash());
            case 6:
                return - arg0.getUser().compareTo(arg1.getUser());
            case 7:
                return - arg0.getUser().getHostname().compareTo(
                        arg1.getUser().getHostname());
            default:
                return 0;
            }
        case NOT_SORTED:
        default:
            return events.indexOf(arg1) - events.indexOf(arg0);
        }
    }

    protected SearchEvent getRow(int i) {
        return events.get(i);
    }

    public enum Sorting {
        DESCENDING, NOT_SORTED, ASCENDING
    }
}
