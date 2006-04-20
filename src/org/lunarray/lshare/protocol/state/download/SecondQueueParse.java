package org.lunarray.lshare.protocol.state.download;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/* TODO rewrite manager so that queue is per user
 * This does the actual parsing work
 * First get all requesteds
 * requested file is directly requested file
 * 
 * then try to handle queued
 * if queue (for a user) is finished, handle stoppeds
 * 
 * get some very infrequent kick to check queue, just to make sure.
 * 
 */

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
     * Constructs the queue parser.
     * @param m The download manager.
     */
    public SecondQueueParse(DownloadManager m) {
        ischecking = false;
        manager = m;
        requests = new ArrayList<IncompleteFile>();
    }

    /**
     * Used to directly request a file.
     * @param inc
     */
    public void directRequest(IncompleteFile inc) {
        requests.add(inc);
    }

    // TODO make regular task to check
    /**
     * Runs the checks.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        while (true) {
            check: {
                if (ischecking) {
                    break check;
                } else {
                    ischecking = true;
                }

                TreeSet<User> available = new TreeSet<User>();
                for (IncompleteFile i : manager.getQueue()) {
                    available.addAll(i.getSources());
                }
                for (DownloadHandler t : manager.getTransfers()) {
                    available.remove(t.getUser());
                }

                for (IncompleteFile f : requests) {
                    download(f, c, available);
                }
                requests.clear();

                for (IncompleteFile i : manager.getQueue()) {
                    if (i.getStatus() == QueueStatus.QUEUED) {
                        download(i, c, available);
                    }
                }

                ischecking = false;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                // Ignore
            }
        }
    }

    /**
     * Initiates the process for downloading a file.
     * @param f The file to download.
     * @param c The controls of the protocol.
     * @param available The available users.
     */
    private void download(IncompleteFile f, Controls c, Set<User> available) {
        for (User u : available) {
            if (f.canDownloadFromUser(u)) {
                try {
                    RemoteFile i = f.getSourceFromUser(u);
                    DownloadHandler h = new DownloadHandler(u, i, f, c, manager);
                    h.init();
                } catch (UserNotFound unf) {
                    // Ignore
                }
            }
        }
    }
}
