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
package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.search.SearchList;
import org.lunarray.lshare.protocol.state.sharing.ShareList;
import org.lunarray.lshare.protocol.state.upload.UploadManager;
import org.lunarray.lshare.protocol.state.userlist.UserList;

/**
 * Represents the state of the protocol.
 * @author Pal Hargitai
 */
public class State {
    /**
     * The user manager.
     */
    private UserList ulist;

    /**
     * The share manager.
     */
    private ShareList slist;

    /**
     * The search manager.
     */
    private SearchList elist;

    /**
     * The download manager.
     */
    private DownloadManager dman;

    /**
     * The upload manager.
     */
    private UploadManager uman;

    /**
     * Initialises the state.
     * @param c The controlls to provide access to the protocol.
     */
    public void init(Controls c) {
        ulist = new UserList(c);
        slist = new ShareList(c);
        elist = new SearchList(c);
        dman = new DownloadManager(c);
        uman = new UploadManager(c);
    }

    /**
     * Commits settings to persistent storage.
     */
    public void commit() {
        dman.close();
        uman.close();
    }

    /**
     * Gets the user manager.
     * @return The user manager.
     */
    public UserList getUserList() {
        return ulist;
    }

    /**
     * Gets the share manager.
     * @return The share manager.
     */
    public ShareList getShareList() {
        return slist;
    }

    /**
     * Gets the search manager.
     * @return The search manager.
     */
    public SearchList getSearchList() {
        return elist;
    }

    /**
     * Gets the download manager.
     * @return The download manager.
     */
    public DownloadManager getDownloadManager() {
        return dman;
    }

    /**
     * Gets the upload manager.
     * @return The upload manager.
     */
    public UploadManager getUploadManager() {
        return uman;
    }
}
