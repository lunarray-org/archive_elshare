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
package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * The search list interface to be used from an application using the protocol.
 * @author Pal Hargitai
 */
public interface ExternalShareList {
    /**
     * Gets a file entry for a given file.
     * @param f The file to get the entry from.
     * @return The entry representing the file.
     * @throws FileNotFoundException Thrown if the file is not found in the
     * path.
     */
    public ShareEntry getEntryFor(File f) throws FileNotFoundException;

    /**
     * Gets all entries that match a given search string.
     * @param s The search string to get the entries for.
     * @return The entries matching the given search string.
     */
    public List<ShareEntry> getEntriesMatching(String s);

    /**
     * Get all the child entries in a given path.
     * @param path The path to get entries of.
     * @return All child entries in a given path.
     * @throws FileNotFoundException Thrown if the path does not exist.
     */
    public List<ShareEntry> getChildrenIn(String path)
            throws FileNotFoundException;

    /**
     * Gets all base entries for that are shared.
     * @return All shared root entries.
     */
    public List<ShareEntry> getBaseEntries();

    /**
     * A share name to unshare.
     * @param sname The share to remove.
     */
    public void removeShare(String sname);

    /**
     * Shares a given path.
     * @param sname The name of the path to share.
     * @param fpath The path to share.
     */
    public void addShare(String sname, File fpath);
}
