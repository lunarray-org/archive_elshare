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

/**
 * An upload listener.
 * @author Pal Hargitai
 */
public interface UploadListener {
    /**
     * Triggered when an upload has been added.
     * @param e The event associated with the upload.
     */
    public void uploadAdded(UploadEvent e);

    /**
     * Triggered when an upload has been removed.
     * @param e The event associated with the upload.
     */
    public void uploadRemoved(UploadEvent e);

    /**
     * Triggered when an upload has been updated.
     * @param e The event associated with the upload.
     */
    public void uploadUpdated(UploadEvent e);
}
