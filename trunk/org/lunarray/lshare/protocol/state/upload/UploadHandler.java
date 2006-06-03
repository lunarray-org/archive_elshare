package org.lunarray.lshare.protocol.state.upload;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.download.ResponseOut;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * Handles setting up of an upload.
 * @author Pal Hargitai
 */
public class UploadHandler implements RunnableTask {
    /**
     * The upload manager
     */
    private UploadManager manager;

    /**
     * The request for upload to manage.
     */
    private UploadRequest request;

    /**
     * The actual upload.
     */
    private UploadTransfer transfer;

    /**
     * The user to upload to.
     */
    private User user;

    /**
     * Constructs an upload handler.
     * @param m The upload manager.
     * @param r The request to handle.
     * @param u The user that requested the upload.
     */
    public UploadHandler(UploadManager m, UploadRequest r, User u) {
        manager = m;
        request = r;
        user = u;
    }

    /**
     * Handles the request.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        try {
            try {
                transfer = new UploadTransfer(c.getState().getShareList()
                        .getFileForEntry(request), request.getOffset(),
                        manager, request, user);
            } catch (FileNotFoundException fnfe) {
                // TODO send file not found
            }

            int port = transfer.init();
            Controls.getLogger().finer(
                    "Opened transfer on: " + Integer.valueOf(port).toString());
            c.getTasks().backgroundTask(transfer);

            // Send response
            try {
                ResponseOut ro = new ResponseOut(request, port, user, request
                        .getOffset());
                c.getUDPTransport().send(ro);

                // Sleep for a while
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    // Ignore
                }
                if (!transfer.isDone()) {
                    if (!transfer.isRunning()) {
                        // If it's neither done nor running, stop.
                        Controls.getLogger().warning("Transfer not running.");
                        transfer.close();
                    }
                } else {
                    Controls.getLogger().info("Transfer done.");
                }

            } catch (UserNotFound nfu) {
                // Ignore this
            }
        } catch (IOException ie) {
            transfer.close();
            Controls.getLogger().warning("Could transfer!");
        }
    }
}
