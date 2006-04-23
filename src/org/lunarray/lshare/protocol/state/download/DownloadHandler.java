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
     * Close down the handler and transfer.
     */
    public void close() {
        if (transfer != null) {
            transfer.close();
            if (chunk.isLocked()) {
                chunk.unlock();
            }
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
    public boolean canHandle(User u, FileResponse f) {
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
    public void handle(User u, FileResponse f) {
        if (canHandle(u, f)) {
            transfer = new DownloadTransfer(chunk, u, this, f.getPort());
            try {
                transfer.init();
                status = DownloadHandlerStatus.RUNNING;
                incomplete.setStatus(QueueStatus.RUNNING);
                
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
    public void init() {
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
                    incomplete.setStatus(QueueStatus.CONNECTING);
                    manager.updatedFile(incomplete);
                    break;
                default:
                    // let it be
                }
                controls.getUDPTransport().send(ro);
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
     * The transfer is done transferring. Clean up.
     */
    protected void done() {
        close();
        manager.removeDownloadHandler(this);

        if (manager.soleFile(incomplete)) {
            incomplete.setStatus(QueueStatus.STOPPED);
            manager.updatedFile(incomplete);
        }
        
        if (chunk.getFile().isFinished()) {
            // TODO Check hash
            manager.removeFromQueue(incomplete);
            Controls.getLogger().info("Transfer done.");
        } else {
            //init(); <- I'm guessing this state should allow this, unsure.
            // It would be better to introduce a queuing mechanism to rerequest this again
        }
    }
}
