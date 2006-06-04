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

import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A representation of a transferring item.
 * @author Pal Hargitai
 */
public interface TransferItem {

    /**
     * Get the amount todo.
     * @return The amount todo.
     */
    public long getTodo();

    /**
     * Get the amount done.
     * @return The amount done.
     */
    public long getDone();

    /**
     * Get the total size of the transfer.
     * @return The total size.
     */
    public long getSize();

    /**
     * Get the remote entry.
     * @return The remote entry.
     */
    public RemoteFile getRemoteEntry();

    /**
     * Get the remote user.
     * @return The remote user.
     */
    public User getRemoteUser();

    /**
     * Update the progressbar.
     */
    public void updateBar();

    /**
     * Get the progressbar.
     * @return The progressbar.
     */
    public JProgressBar getProgressBar();

    /**
     * Get the local file.
     * @return The local file.
     */
    public File getLocal();

    /**
     * Get the status of the transfer.
     * @return The status of the transfer.
     */
    public String getStatus();

    /**
     * Get the transfer itself.
     * @return The transfer.
     */
    public Object getTransfer();

    /**
     * Cancel the transfer.
     */
    public void cancel();
}
