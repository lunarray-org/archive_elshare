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

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.events.DownloadEvent;
import org.lunarray.lshare.protocol.events.DownloadListener;
import org.lunarray.lshare.protocol.events.UploadEvent;
import org.lunarray.lshare.protocol.events.UploadListener;

/**
 * The model for file transfers.
 * @author Pal Hargitai
 */
public class TransferModel implements TableModel, UploadListener,
        DownloadListener, TableCellRenderer {

    /**
     * The transfer items.
     */
    private ArrayList<TransferItem> transferitems;

    /**
     * The listeners.
     */
    private ArrayList<TableModelListener> listeners;

    /**
     * Constructs the transfer model.
     */
    public TransferModel() {
        transferitems = new ArrayList<TransferItem>();
        listeners = new ArrayList<TableModelListener>();
    }

    /**
     * Gets the cell renderer for the progressbars.
     * @param arg0 The table, we assume there is just one.
     * @param arg1 The value to return.
     * @param arg2 Wether it is selected
     * @param arg3 Wether it has focus
     * @param arg4 The row.
     * @param arg5 The column.
     */
    public Component getTableCellRendererComponent(JTable arg0, Object arg1,
            boolean arg2, boolean arg3, int arg4, int arg5) {
        return arg5 == 3 ? transferitems.get(arg4).getProgressBar() : null;
    }

    /**
     * Get the amount of rows.
     * @return The amount of rows.
     */
    public int getRowCount() {
        return transferitems.size();
    }

    /**
     * Get the amount of columns.
     * @return The amount of columns.
     */
    public int getColumnCount() {
        // User
        // Remoteentry
        // Status
        // Progress
        // Localentry
        // Size
        return 6;
    }

    /**
     * Get the column names.
     * @param arg0 The index of the column.
     * @return The name of the column.
     */
    public String getColumnName(int arg0) {
        switch (arg0) {
        case 0:
            return "User";
        case 1:
            return "Remote file";
        case 2:
            return "Status";
        case 3:
            return "Progress";
        case 4:
            return "Filename";
        case 5:
            return "Size";
        default:
            return "";
        }
    }

    /**
     * Get the class of a column.
     * @param arg0 The index of the column.
     * @return The class of the column.
     */
    public Class<?> getColumnClass(int arg0) {
        return arg0 == 3 ? JProgressBar.class : String.class;
    }

    /**
     * Get the value of a cell.
     * @param arg1 The row of the cell.
     * @param arg0 The column of the cell.
     * @return The item at the cell.
     */
    public Object getValueAt(int arg1, int arg0) {
        if (arg1 >= 0 && arg1 < transferitems.size()) {
            switch (arg0) {
            case 0:
                return transferitems.get(arg1).getRemoteUser().toString();
            case 1:
                return transferitems.get(arg1).getRemoteEntry().toString();
            case 2:
                return transferitems.get(arg1).getStatus();
            case 3:
                return transferitems.get(arg1).getProgressBar();
            case 4:
                return transferitems.get(arg1).getLocal();
            case 5:
                return GUIUtil.prettyPrint(transferitems.get(arg1).getSize());
            default:
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Test if a cell is editable.
     * @param arg0 The row.
     * @param arg1 The column.
     * @return Wether the cell is editable. Generally false.
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /**
     * Set the value of a cell.
     * @param arg0 The new value.
     * @param arg1 The row.
     * @param arg2 The column.
     */
    public void setValueAt(Object arg0, int arg1, int arg2) {
        // Ignore
    }

    /**
     * Add a model listener.
     * @param arg0 The listener to add.
     */
    public void addTableModelListener(TableModelListener arg0) {
        listeners.add(arg0);
    }

    /**
     * Remove a model lisener.
     * @param arg0 The listener to remove.
     */
    public void removeTableModelListener(TableModelListener arg0) {
        listeners.remove(arg0);
    }

    /**
     * Triggered if a download is added.
     * @param e The event associated with the add.
     */
    public synchronized void downloadAdded(DownloadEvent e) {
        DownloadItem t = new DownloadItem(e.getTransfer());
        transferitems.add(t);

        int i = transferitems.indexOf(t);

        TableModelEvent ev = new TableModelEvent(this, i, i,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        for (TableModelListener l : listeners) {
            l.tableChanged(ev);
        }
    }

    /**
     * Triggered if a download is removed.
     * @param e The event associated with the remove.
     */
    public synchronized void downloadRemoved(DownloadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            int i = transferitems.indexOf(torem);
            transferitems.remove(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    /**
     * Triggered if a download is updated.
     * @param e The event associated with the update.
     */
    public synchronized void downloadUpdated(DownloadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            // Update
            int i = transferitems.indexOf(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    /**
     * Triggered if a upload is added.
     * @param e The event associated with the add.
     */
    public synchronized void uploadAdded(UploadEvent e) {
        UploadItem t = new UploadItem(e.getTransfer());
        transferitems.add(t);

        int i = transferitems.indexOf(t);

        TableModelEvent ev = new TableModelEvent(this, i, i,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        for (TableModelListener l : listeners) {
            l.tableChanged(ev);
        }

    }

    /**
     * Triggered if a upload is removed.
     * @param e The event associated with the remove.
     */
    public synchronized void uploadRemoved(UploadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            int i = transferitems.indexOf(torem);
            transferitems.remove(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    /**
     * Triggered if a upload is updated.
     * @param e The event associated with the update.
     */
    public synchronized void uploadUpdated(UploadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            // Update
            int i = transferitems.indexOf(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    /**
     * Get the transfer at a specified row.
     * @param i The index of the transfer.
     * @return The transfer.
     */
    protected TransferItem getRow(int i) {
        return transferitems.get(i);
    }

    /**
     * Updates the table.
     */
    protected void updateTable() {
        for (int i = 0; i < transferitems.size(); i++) {
            transferitems.get(i).updateBar();

            TableModelEvent ev = new TableModelEvent(this, i, i, 3,
                    TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }
}
