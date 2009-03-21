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
package org.lunarray.lshare.protocol.filelist;

import java.util.List;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A file entry recieved over a tcp connection.
 * @author Pal Hargitai
 */
public class FilelistEntry extends RemoteFile {
    /**
     * The filelist receiver for populating file entries.
     */
    private FilelistReceiver receiver;

    /**
     * True if the root is a root node for populating the correct children.
     */
    private boolean isroot;

    /**
     * Constructs a file entry.
     * @param fr The list receiver for getting child entries.
     * @param p The path that this entry resides in.
     * @param n The name of this entry.
     * @param h The hash of this entry.
     * @param lm The last modified date of this entry.
     * @param s The size of this entry.
     * @param root True if this is the root node, false if not.
     */
    public FilelistEntry(FilelistReceiver fr, String p, String n, Hash h,
            long lm, long s, boolean root) {
        super(p, n, h, lm, s);
        receiver = fr;
        isroot = root;
    }

    /**
     * Close down the receiver.
     */
    public void closeReceiver() {
        receiver.close();
    }

    /**
     * Get all children in this entry.
     * @return The child entries in this entry.
     */
    public List<FilelistEntry> getEntries() {
        return receiver.getEntries(
                getPath() + RemoteFile.SEPARATOR + getName(), isroot);
    }
}
