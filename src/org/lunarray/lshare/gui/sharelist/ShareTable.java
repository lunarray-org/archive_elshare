package org.lunarray.lshare.gui.sharelist;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;

public class ShareTable implements TableModel {
	
	private ArrayList<TableModelListener> listeners;
	private ArrayList<SharedDirectory> dirs;
	private LShare lshare;
	
	public ShareTable(LShare ls) {
		listeners = new ArrayList<TableModelListener>();
		dirs = new ArrayList<SharedDirectory>();
		lshare = ls;
		init();
	}
	
	public void refresh() {
		dirs.clear();
		init();
		for (TableModelListener t: listeners) {
			TableModelEvent e = new TableModelEvent(this);
			t.tableChanged(e);
		}
	}
	
	private void init() {
		for (SharedDirectory d: lshare.getShareList().getShares()) {
			dirs.add(d);
		}
	}

	public void addTableModelListener(TableModelListener arg0) {
		listeners.add(arg0);		
	}
	
	public void removeTableModelListener(TableModelListener arg0) {
		listeners.remove(arg0);
	}
	
	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}
	
	public int getColumnCount() {
		return 2;
	}
	
	public String getColumnName(int arg0) {
		switch (arg0) {
		case 0:
			return "Name";
		case 1:
			return "Path";
		default:
			return "";
		}
	}
	
	public int getRowCount() {
		return dirs.size();
	}
	
	public Object getValueAt(int arg0, int arg1) {
		if (0 <= arg0 && arg0 < dirs.size()) {
			switch (arg1) {
			case 0:
				return dirs.get(arg0).getName();
			case 1:
				return dirs.get(arg0).getFilePath();
			default:
				return "";
			}
		} else {
			return "";
		}
	}
	
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
	
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// Ignore
	}
	
	public String getNameAtRow(int i) {
		if (0 <= i && i < dirs.size()) {
			return dirs.get(i).getName();
		} else {
			return "";
		}
	}
}
