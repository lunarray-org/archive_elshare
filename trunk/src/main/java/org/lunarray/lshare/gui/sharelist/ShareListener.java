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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;

/**
 * The list selection listener to selection in the sharelist model.
 * @author Pal Hargitai
 */
public class ShareListener implements ListSelectionListener {
    /**
     * The model that is associated with this listener.
     */
    private ShareTable model;

    /**
     * The name of the share to remove, if any.
     */
    private String name;

    /**
     * The instance of the protocol to communicate with.
     */
    private LShare lshare;

    /**
     * Constructs a share listener.
     * @param st The table model to get data from.
     * @param ls The instance of the procotol to use.
     */
    public ShareListener(ShareTable st, LShare ls) {
        model = st;
        name = "";
        lshare = ls;
    }

    /**
     * A selection has occured.
     * @param arg0 The selection event.
     */
    public void valueChanged(ListSelectionEvent arg0) {
        name = model.getNameAtRow(arg0.getFirstIndex());
    }

    /**
     * Get the name of the share of the selected row.
     * @return The name of the selected row.
     */
    public String getSelectedName() {
        return name;
    }

    /**
     * Remove the selected row.
     */
    public void removeSelected() {
        if (name.length() > 0) {
            lshare.getShareList().removeShare(name);
            model.refresh();
        }
    }
}
