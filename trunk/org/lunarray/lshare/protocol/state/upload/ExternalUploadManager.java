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
package org.lunarray.lshare.protocol.state.upload;

import java.util.List;

import org.lunarray.lshare.protocol.events.UploadListener;

/**
 * An external interface for upload management.
 * @author Pal Hargitai
 */
public interface ExternalUploadManager {
    /**
     * Sets the amount of available upload slots.
     * @param s The new amount of upload slots.
     */
    public void setSlots(int s);

    /**
     * Gets the amount of available upload slots.
     * @return The amount of upload slots.
     */
    public int getSlots();

    /**
     * Set the download rate.
     * @param r The new download rate.
     */
    public void setRate(int r);

    /**
     * Get the download rate.
     * @return The download rate.
     */
    public int getRate();

    /**
     * Gets a list of all uploads.
     * @return All known uploads.
     */
    public List<UploadTransfer> getUploads();

    /**
     * Register a listener.
     * @param lis The listener to register.
     */
    public void addListener(UploadListener lis);

    /**
     * Unregisteres a listener.
     * @param lis The listener to unregister.
     */
    public void removeListener(UploadListener lis);
}
