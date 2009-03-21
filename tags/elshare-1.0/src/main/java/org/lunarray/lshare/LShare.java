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
package org.lunarray.lshare;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.ExternalSettings;
import org.lunarray.lshare.protocol.state.download.ExternalDownloadManager;
import org.lunarray.lshare.protocol.state.search.ExternalSearchList;
import org.lunarray.lshare.protocol.state.sharing.ExternalShareList;
import org.lunarray.lshare.protocol.state.upload.ExternalUploadManager;
import org.lunarray.lshare.protocol.state.userlist.ExternalUserList;

/**
 * Provides access to an instance of the eLShare protocol. Provides an
 * abstraction to the protocol.
 * @author Pal Hargitai
 */
public class LShare {
    /**
     * The standard controls that should be visible throughout the system.
     */
    private Controls controls;

    /**
     * Instanciates the protocol.
     */
    public LShare() {
        controls = new Controls();
    }

    /**
     * Provides access to an abstraction of the standard userlist.
     * @return An abstraction of the userlist bound to this instance of the
     * protocol.
     */
    public ExternalUserList getUserList() {
        return controls.getState().getUserList();
    }

    /**
     * Provides access to an abstraction of the standard settings.
     * @return An abstraction of the settings bound to this instance of the
     * protocol.
     */
    public ExternalSettings getSettings() {
        return controls.getSettings();
    }

    /**
     * Provides access to an abstraction of the list of shared directories.
     * @return An abstraction of the list of shared directories.
     */
    public ExternalShareList getShareList() {
        return controls.getState().getShareList();
    }

    /**
     * Provides access to an abstraction of the search functionality of the
     * protocol.
     * @return An abstraction of the search functionality of the protocol.
     */
    public ExternalSearchList getSearchList() {
        return controls.getState().getSearchList();
    }

    /**
     * Provides access to an abstraction of the download manager of the
     * protocol.
     * @return An abstraction of the download manager of the protocol.
     */
    public ExternalDownloadManager getDownloadManager() {
        return controls.getState().getDownloadManager();
    }

    /**
     * Provides access to an abstraction of the upload manager of the protocol.
     * @return An abstractin of the upload manager of the protocol.
     */
    public ExternalUploadManager getUploadManager() {
        return controls.getState().getUploadManager();
    }

    /**
     * Starts the protocol and all underlying layers. That provide it's
     * functionality.
     */
    public void start() {
        controls.start();
    }

    /**
     * Stops the protocol and all underlying layers. Tries to close the protocol
     * as neatly as possible.
     */
    public void stop() {
        controls.stop();
    }
}