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