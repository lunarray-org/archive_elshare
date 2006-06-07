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

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A request for uploading a file.
 * @author Pal Hargitai
 */
public class UploadRequest extends RemoteFile {
    /**
     * The offset at which to start uploading.
     */
    private long offset;

    /**
     * Constructs the upload request.
     * @param p The path of the file.
     * @param n The name of the file.
     * @param h The hash of the file.
     * @param s The size of the file.
     * @param o The offset at which to start uploading.
     */
    public UploadRequest(String p, String n, Hash h, long s, long o) {
        super(p, n, h, 1, s);
        offset = o;
    }

    /**
     * The offset at which to start uploading.
     * @return The offset.
     */
    public long getOffset() {
        return offset;
    }
}