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
package org.lunarray.lshare.protocol.state.download.file;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A remote file that is localy queued.
 * @author Pal Hargitai
 */
public class QueuedRemote extends RemoteFile {
    /**
     * Constructs a queued entry.
     * @param path The path of the file.
     * @param name The name of the file.
     * @param h The hash of the file.
     * @param size The size of the file.
     */
    public QueuedRemote(String path, String name, Hash h, long size) {
        super(path, name, h, 1, size);
    }
}
