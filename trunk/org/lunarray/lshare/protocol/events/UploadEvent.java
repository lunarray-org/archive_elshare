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
package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.upload.UploadManager;
import org.lunarray.lshare.protocol.state.upload.UploadTransfer;

/**
 * An upload event.
 * @author Pal Hargitai
 */
public class UploadEvent {
    /**
     * The transfer in the event.
     */
    private UploadTransfer transfer;

    /**
     * The source of the event.
     */
    private UploadManager source;

    /**
     * Constructs an upload event.
     * @param t The transfer of the event.
     * @param m The manager of the event.
     */
    public UploadEvent(UploadTransfer t, UploadManager m) {
        transfer = t;
        source = m;
    }

    /**
     * Gets the transfer.
     * @return The transfer.
     */
    public UploadTransfer getTransfer() {
        return transfer;
    }

    /**
     * Gets the source of the event. That is, the upload manager.
     * @return The source of the event.
     */
    public UploadManager getSource() {
        return source;
    }
}
