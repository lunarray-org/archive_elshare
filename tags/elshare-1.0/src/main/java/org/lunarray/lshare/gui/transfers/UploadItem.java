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

import java.awt.Color;
import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.upload.UploadTransfer;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * An item representing an upload.
 * @author Pal Hargitai
 */
public class UploadItem implements TransferItem {

    /**
     * The upload to represent.
     */
    private UploadTransfer upload;

    /**
     * The progressbar.
     */
    private JProgressBar progressbar;

    /**
     * Constructs an upload item by a given transfer.
     * @param dh The transfer to construct the item of.
     */
    public UploadItem(UploadTransfer dh) {
        upload = dh;
        progressbar = new JProgressBar();

        progressbar.setStringPainted(true);
        progressbar.setBackground(Color.RED);
        progressbar.setForeground(Color.RED.darker());

        progressbar.setMinimum(0);
        if (getSize() > Integer.MAX_VALUE) {
            progressbar.setMaximum(Long.valueOf(getSize() / Integer.MAX_VALUE)
                    .intValue());
            progressbar.setValue(Long.valueOf(getDone() / Integer.MAX_VALUE)
                    .intValue());
        } else {
            progressbar.setMaximum(Long.valueOf(getSize()).intValue());
            progressbar.setValue(Long.valueOf(getDone()).intValue());
        }
    }

    /**
     * Get the transfer itself.
     * @return The transfer.
     */
    public Object getTransfer() {
        return upload;
    }

    /**
     * Get the status of the transfer.
     * @return The status of the transfer.
     */
    public String getStatus() {
        if (upload.isDone()) {
            return "Finished";
        } else {
            return upload.isRunning() ? "Transferring" : "Connecting";
        }
    }

    /**
     * Get the local file.
     * @return The local file.
     */
    public File getLocal() {
        return upload.getFile();
    }

    /**
     * Get the amount todo.
     * @return The amount todo.
     */
    public long getTodo() {
        return upload.getTodo();
    }

    /**
     * Get the transferred amount.
     * @return The transferred amount.
     */
    public long getDone() {
        return upload.getDone();
    }

    /**
     * Get the total size of the transfer.
     * @return The total size of the transfer.
     */
    public long getSize() {
        return upload.getSize();
    }

    /**
     * Get the remote entry of the file.
     * @return The remote entry of the file.
     */
    public RemoteFile getRemoteEntry() {
        return upload.getRequest();
    }

    /**
     * Get the remote user.
     * @return The remote user.
     */
    public User getRemoteUser() {
        return upload.getUser();
    }

    /**
     * Update the progressbar.
     */
    public void updateBar() {
        progressbar.setValue(Long.valueOf(
                getSize() > Integer.MAX_VALUE ? getDone() / Integer.MAX_VALUE
                        : getDone()).intValue());
    }

    /**
     * Get the progressbar.
     * @return The progressbar.
     */
    public JProgressBar getProgressBar() {
        return progressbar;
    }

    /**
     * Cancel the transfer.
     */
    public void cancel() {
        upload.cancel();
    }
}
