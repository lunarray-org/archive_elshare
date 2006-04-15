package org.lunarray.lshare.gui.search;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.TableModel;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.events.SearchEvent;

public class SearchModel implements TableModel {

	/**
	 * The events that have been processed, seen as search results.
	 */
	private ArrayList<SearchEvent> events;
	
	/**
	 * The listeners to this model.
	 */
	private ArrayList<TableModelListener> listeners;
	
	/**
	 * Constructs the search model.
	 */
	public SearchModel() {
		events = new ArrayList<SearchEvent>();
		listeners = new ArrayList<TableModelListener>();
	}
	
	/**
	 * Processes a search event.
	 * @param e The search event to process.
	 */
	public void processEvent(SearchEvent e) {
		search: {
			System.out.println(e.getEntry().getName());
			for (SearchEvent ev: events) {
				if (e.getEntry().getName().equals(ev.getEntry().getName()) &&
						e.getEntry().getPath().equals(ev.getEntry().getPath()) &&
						e.getUser().getAddress().equals(ev.getUser().getAddress())) {
					break search;
				}
			}
			events.add(e);
			for (TableModelListener l: listeners) {
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
				return GUIUtil.stripDirPath(events.get(arg1).getEntry().getPath());
			} else {
				return events.get(arg1).getEntry().getPath();
			}			
		case 3:
			if (events.get(arg1).getEntry().getSize() >= 0) {
				return GUIUtil.prettyPrint(events.get(arg1).getEntry().getSize());
			} else {
				return "";
			}
		case 4:
			if (events.get(arg1).getEntry().getLastModified() == 0) {
				return "";
			} else {
				return (new Date(events.get(arg1).getEntry().getLastModified())).toString();
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
}
