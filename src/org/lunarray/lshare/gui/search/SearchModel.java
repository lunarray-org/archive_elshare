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

	private ArrayList<SearchEvent> events;
	private ArrayList<TableModelListener> listeners;
	
	public SearchModel() {
		events = new ArrayList<SearchEvent>();
		listeners = new ArrayList<TableModelListener>();
	}
	
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
	
	public int getRowCount() {
		return events.size();
	}

	public int getColumnCount() {
		return 8;
	}

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

	public Class<?> getColumnClass(int arg0) {
		if (arg0 == 0) {
			return Icon.class;
		} else {
			return String.class;
		}
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

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
			return GUIUtil.hashToString(events.get(arg1).getEntry().getHash());
		case 6:
			return events.get(arg1).getUser().getName();
		case 7:
			return events.get(arg1).getUser().getHostname();
		default:
			return "";
		}
	}

	public void setValueAt(Object arg0, int arg1, int arg2) {
		// Ignore
	}

	public void addTableModelListener(TableModelListener arg0) {
		listeners.add(arg0);
	}

	public void removeTableModelListener(TableModelListener arg0) {
		listeners.remove(arg0);
	}
}
