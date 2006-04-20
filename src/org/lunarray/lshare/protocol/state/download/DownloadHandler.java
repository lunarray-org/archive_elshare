package org.lunarray.lshare.protocol.state.download;

import java.io.IOException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.download.RequestOut;
import org.lunarray.lshare.protocol.state.download.file.Chunk;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

public class DownloadHandler {

    private User user;

    private RemoteFile remote;

    private IncompleteFile incomplete;

    private Controls controls;

    private DownloadManager manager;

    private DownloadTransfer transfer;

    private Chunk chunk;

    private DownloadHandlerStatus status;

    public DownloadHandler(User u, RemoteFile f, IncompleteFile i, Controls c,
            DownloadManager m) {
        user = u;
        remote = f;
        incomplete = i;
        controls = c;
        manager = m;
        status = DownloadHandlerStatus.INIT;
    }

    public void close() {
        if (transfer != null) {
            transfer.close();
            if (chunk.isLocked()) {
                chunk.unlock();
            }
        }
    }

    public User getUser() {
        return user;
    }

    protected DownloadHandlerStatus getStatus() {
        return status;
    }

    public boolean canHandle(User u, FileResponse f) {
        if (u.equals(user)) {
            if (f.getHash().equals(remote.getHash())) {
                if (chunk.getMark() == f.getOffset()) {
                    if (f.getName().equals(remote.getName())
                            && f.getPath().equals(remote.getPath())) {
                        return true;
                    } else {
                        System.out.println("name and path didn't match");
                        return false;
                    }
                } else {
                    System.out.println("offset didn't match");
                    return false;
                }
            } else {
                System.out.println("hash didn't match");
                return false;
            }
        } else {
            System.out.println("user didn't match");
            return false;
        }
    }

    public void handle(User u, FileResponse f) {
        if (canHandle(u, f)) {
            transfer = new DownloadTransfer(chunk, u, this, f.getPort());
            try {
                transfer.init();
                status = DownloadHandlerStatus.RUNNING;
                controls.getTasks().backgroundTask(transfer);
            } catch (IOException ie) {
                // Something went wrong, cleanup
                Controls.getLogger().warning("Error connecting to user.");
                close();
                manager.removeDownloadHandler(this);
            }
        }
    }

    protected void done() {
        close();
        manager.removeDownloadHandler(this);

        if (chunk.getFile().isFinished()) {
            // TODO Check hash
            manager.removeFromQueue(incomplete);
            Controls.getLogger().info("Transfer done.");
        } else {
            init(); // I'm guessing this state should allow this, unsure.
        }
    }

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
                controls.getUDPTransport().send(ro);
                status = DownloadHandlerStatus.CONNECTING;
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
}
