package org.lunarray.lshare.protocol.state.download;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * Handles the queue and goes on to download from all online users.
 * @author Pal Hargitai
 */
public class SecondQueueParse implements RunnableTask {
    /**
     * Wether a check is in progress. True if the check is in progress, false if
     * not.
     */
    private boolean ischecking;

    /**
     * Directly requested files for immediate transfer.
     */
    private ArrayList<IncompleteFile> requests;

    /**
     * The download manager.
     */
    private DownloadManager manager;

    /**
     * Set to true if the parser should run.
     */
    private boolean shouldrun;

    /**
     * Constructs the queue parser.
     * @param m The download manager.
     */
    public SecondQueueParse(DownloadManager m) {
        ischecking = false;
        manager = m;
        requests = new ArrayList<IncompleteFile>();
        shouldrun = true;
    }

    /**
     * Used to directly request a file.
     * @param inc
     */
    public void directRequest(IncompleteFile inc) {
        requests.add(inc);
    }

    /**
     * Runs the checks.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        run: {
            while (true) {
                check: {
                    if (ischecking) {
                        break check;
                    } else {
                        ischecking = true;
                    }

                    // Check if there are requests, and request files if there
                    // are not already transfers with the user going on.
                    if (!requests.isEmpty()) {
                        ArrayList<IncompleteFile> toremove = new ArrayList<IncompleteFile>();
                        for (IncompleteFile f : requests) {
                            for (User u : f.getSources()) {
                                request: {
                                    for (DownloadHandler t : manager
                                            .getTransfers()) {
                                        if (t.getUser().equals(u)) {
                                            break request;
                                        }
                                    }

                                    // We can request now
                                    download(f, c, u);
                                    toremove.add(f);
                                }
                            }
                        }
                        for (IncompleteFile f : toremove) {
                            requests.remove(f);
                        }
                    }

                    for (User u : manager.getQueuedUsers()) {
                        request: {
                            for (DownloadHandler t : manager.getTransfers()) {
                                if (t.getUser().equals(u)) {
                                    break request;
                                }
                            }

                            // No transfers from user, get do a normal request.
                            while (!manager.getQueueFromUser(u).isEmpty()) {
                                IncompleteFile f = manager.getQueueFromUser(u)
                                        .get(0);
                                switch (f.getStatus()) {
                                case FINISHED:
                                    manager.removeFromQueue(f);
                                    break;
                                case QUEUED:
                                case RUNNING:
                                case STOPPED:
                                    download(f, c, u);
                                    break request;
                                default:
                                    // Ignore
                                    break request;
                                }
                            }
                        }
                    }

                    ischecking = false;
                }
                if (!shouldrun) {
                    break run;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    // Acknowledge that it's been interrupted
                    Thread.interrupted();
                }
            }
        }
    }

    /**
     * Close down the thread.
     */
    public void close() {
        shouldrun = false;
    }

    /**
     * Initiates the process for downloading a file.
     * @param f The file to download.
     * @param c The controls of the protocol.
     * @param u The user to download from.
     */
    private void download(IncompleteFile f, Controls c, User u) {
        try {
            RemoteFile i = f.getSourceFromUser(u);
            DownloadHandler h = new DownloadHandler(u, i, f, c, manager);
            h.init();
        } catch (UserNotFound unf) {
            // Ignore
        }
    }
}
