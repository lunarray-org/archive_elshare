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
package org.lunarray.lshare.protocol.state.download;

import java.io.File;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A queued item. Ready for preprocessing.
 * @author Pal Hargitai
 */
public class QueuedItem {
    /**
     * The remote entry to process.
     */
    private RemoteFile file;

    /**
     * The user it resides with.
     */
    private User user;

    /**
     * The directory that this file is to download to.
     */
    private File target;

    /**
     * Constructs a queued item. Ready for preprocessing.
     * @param f The remote entry to download.
     * @param u The user to download from.
     * @param to The directory to download to. Or if file the direct file to
     * download to.
     */
    public QueuedItem(RemoteFile f, User u, File to) {
        file = f;
        user = u;
        target = to;
    }

    /**
     * Gets the remote entry.
     * @return The remote entry.
     */
    public RemoteFile getFile() {
        return file;
    }

    /**
     * Gets the user.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the directory where this file is to download to.
     * @return The target this file is to download to.
     */
    public File getTarget() {
        return target;
    }

    /**
     * Gets the target file. This is the name of the file in the target path.
     * @return The file that this is to download to.
     */
    public File getTargetFile() {
        return target.isDirectory() ? new File(target.getPath()
                + File.separator + file.getName()) : target;
    }
}
