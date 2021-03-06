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

import java.io.IOException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.download.RequestOut;
import org.lunarray.lshare.protocol.state.download.file.Chunk;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * A download handler for handling the setup and cleanup of a single transfer.
 * @author Pal Hargitai
 */
public class DownloadHandler {
    /**
     * The user to download from.
     */
    private User user;

    /**
     * The remote file to download.
     */
    private RemoteFile remote;

    /**
     * The incomplete file to download to.
     */
    private IncompleteFile incomplete;

    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * The download manager
     */
    private DownloadManager manager;

    /**
     * The transfer.
     */
    private DownloadTransfer transfer;

    /**
     * The chunk ot download to.
     */
    private Chunk chunk;

    /**
     * The status of the handler.
     */
    private DownloadHandlerStatus status;

    /**
     * Constructs a download handler.
     * @param u The user to download from.
     * @param f The remote entry to download.
     * @param i The incomplete file to download to.
     * @param c The controls of the protocol.
     * @param m The manager.
     */
    public DownloadHandler(User u, RemoteFile f, IncompleteFile i, Controls c,
            DownloadManager m) {
        user = u;
        remote = f;
        incomplete = i;
        controls = c;
        manager = m;
        status = DownloadHandlerStatus.INIT;
    }

    /**
     * Gets the remote entry.
     * @return The remote entry.
     */
    public RemoteFile getRemoteEntry() {
        return remote;
    }

    /**
     * Close down the handler and transfer.
     */
    public synchronized void close() {
        if (transfer != null) {
            transfer.close();
        }
    }

    /**
     * Get the user that is to be used in this transfer.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Check if the given response is valid.
     * @param u The user that the response came from.
     * @param f The file response.
     * @return True if this is a valid repsonse.
     */
    public synchronized boolean canHandle(User u, FileResponse f) {
        if (u.equals(user)) {
            if (f.getHash().equals(remote.getHash())) {
                if (chunk.getMark() == f.getOffset()) {
                    if (f.getName().equals(remote.getName())
                            && f.getPath().equals(remote.getPath())) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Handle the response from the user.
     * @param u The user from which the response came.
     * @param f The response to handle.
     */
    public synchronized void handle(User u, FileResponse f) {
        if (canHandle(u, f)) {
            transfer = new DownloadTransfer(chunk, u, this, f.getPort());
            try {
                transfer.init();
                status = DownloadHandlerStatus.RUNNING;

                manager.updatedFile(incomplete);
                manager.updatedTransfer(this);

                controls.getTasks().backgroundTask(transfer);
            } catch (IOException ie) {
                // Something went wrong, cleanup
                Controls.getLogger().warning("Error connecting to user.");
                close();
                manager.removeDownloadHandler(this);
            }
        }
    }

    /**
     * Initialise the handler.
     */
    public synchronized void init() {
        if (incomplete.getSources().contains(user)) {
            // Is a valid source
            for (DownloadHandler d : manager.getTransfers()) {
                if (d.getUser().equals(user)) {
                    manager.removeDownloadHandler(this);
                    return;
                }
            }

            // No downloads from the user yet
            // We can start request
            try {
                chunk = incomplete.getChunk();
                RequestOut ro = new RequestOut(user, remote, chunk.getMark());
                chunk.lock();
                switch (incomplete.getStatus()) {
                case QUEUED:
                case STOPPED:
                    manager.updatedFile(incomplete);
                    break;
                default:
                // let it be
                }
                // controls.getUDPTransport().send(ro);
                status = DownloadHandlerStatus.CONNECTING;
                manager.updatedTransfer(this);
                controls.getTasks().enqueueMultiTask(
                        new DownloadTimeout(this, ro));
                manager.addDownloadHandler(this);
            } catch (IllegalAccessException iae) {
                // No chunk
            } catch (UserNotFound nfe) {
                // No user
            }
        } else {
            manager.removeDownloadHandler(this);
        }
    }

    /**
     * Get the status of the handler.
     * @return The status of the handler.
     */
    public DownloadHandlerStatus getStatus() {
        return status;
    }

    /**
     * Gets the incomplete file that is transferring.
     * @return The file.
     */
    public IncompleteFile getFile() {
        return incomplete;
    }

    /**
     * Cancels the filetransfer.
     */
    public synchronized void cancel() {
        transfer.cancel();
    }

    /**
     * The transfer is done transferring. Clean up.
     */
    protected synchronized void done() {
        close();
        manager.removeDownloadHandler(this);

        if (manager.soleFile(incomplete)) {
            manager.updatedFile(incomplete);
        }

        if (chunk.getFile().isFinished()) {
            incomplete.checkIntegrity();
            manager.removeFromQueue(incomplete);
            Controls.getLogger().info("Transfer done.");
        } else {
            // init(); <- I'm guessing this state should allow this, unsure.
            // It would be better to introduce a queuing mechanism to rerequest
            // this again
        }
    }
}
