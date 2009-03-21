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
package org.lunarray.lshare.gui.sharelist;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

/**
 * The table model to contain share information.
 * @author Pal Hargitai
 */
public class ShareTable implements TableModel {
    /**
     * The listeners of this model.
     */
    private ArrayList<TableModelListener> listeners;

    /**
     * The shared directories that are to be shown here.
     */
    private ArrayList<ShareEntry> dirs;

    /**
     * The instance of the protocol to use.
     */
    private LShare lshare;

    /**
     * Constructs the table model.
     * @param ls The instance of the protocol to use.
     */
    public ShareTable(LShare ls) {
        listeners = new ArrayList<TableModelListener>();
        dirs = new ArrayList<ShareEntry>();
        lshare = ls;
        init();
    }

    /**
     * Refreshes the model and loads new shares.
     */
    public void refresh() {
        dirs.clear();
        init();
        for (TableModelListener t : listeners) {
            TableModelEvent e = new TableModelEvent(this);
            t.tableChanged(e);
        }
    }

    /**
     * Adds a listener.
     * @param arg0 The listener to add.
     */
    public void addTableModelListener(TableModelListener arg0) {
        listeners.add(arg0);
    }

    /**
     * Removes a listener.
     * @param arg0 The listener to remove.
     */
    public void removeTableModelListener(TableModelListener arg0) {
        listeners.remove(arg0);
    }

    /**
     * Gets the class of a specified column.
     * @param arg0 The index of the column.
     * @return The class of the colum, generally String.class.
     */
    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    /**
     * The amount of columns.
     * @return Generally two.
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * Get the name of a specified column.
     * @param arg0 The index of the column.
     * @return The name of the specified column.
     */
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

    /**
     * Get the amount of rows.
     * @return The amount of rows.
     */
    public int getRowCount() {
        return dirs.size();
    }

    /**
     * Gets the value of a specific cell.
     * @param arg0 The row of the cell.
     * @param arg1 The column of the cell.
     * @return The value of the cell.
     */
    public Object getValueAt(int arg0, int arg1) {
        if (0 <= arg0 && arg0 < dirs.size()) {
            switch (arg1) {
            case 0:
                return dirs.get(arg0).getName();
            case 1:
                return dirs.get(arg0).getFile().getPath();
            default:
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Checks if a cell is editable.
     * @param arg0 The column.
     * @param arg1 The row.
     * @return False, cells are not generally editable.
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /**
     * Sets the value of a cell. Generally ignored.
     * @param arg0 The value to set a cell to.
     * @param arg1 The row of the cell.
     * @param arg2 The column of the cell.
     */
    public void setValueAt(Object arg0, int arg1, int arg2) {
        // Ignore
    }

    /**
     * Get the name of the share at the specified row.
     * @param i The row number.
     * @return The name of the share.
     */
    public String getNameAtRow(int i) {
        return 0 <= i && i < dirs.size() ? dirs.get(i).getName() : "";
    }

    /**
     * Initialises or reloads the model.
     */
    private void init() {
        dirs.addAll(lshare.getShareList().getBaseEntries());
    }
}
