package org.lunarray.lshare.protocol.state.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.events.DownloadEvent;
import org.lunarray.lshare.protocol.events.DownloadListener;
import org.lunarray.lshare.protocol.events.QueueEvent;
import org.lunarray.lshare.protocol.events.QueueListener;
import org.lunarray.lshare.protocol.state.download.file.DownloadFileManager;
import org.lunarray.lshare.protocol.state.download.file.FileExistsException;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A manager for downloads.
 * @author Pal Hargitai
 */
public class DownloadManager implements RunnableTask, ExternalDownloadManager {
    /**
     * A temporary queue whose items should be processed to the file queue.
     */
    private LinkedBlockingQueue<QueuedItem> tempqueue;

    /**
     * The queue that may be directly processed.
     */
    private TreeMap<User, ArrayList<IncompleteFile>> queue;

    /**
     * The controls of the protocol.
     */
    private Controls controls;

    /**
     * The file manager.
     */
    private DownloadFileManager filemanager;

    /**
     * The known transfers.
     */
    private ArrayList<DownloadHandler> transfers;

    /**
     * The parser of the queue that initialises the downloads.
     */
    private SecondQueueParse secondqueue;

    private Thread firstqueuethread;

    private ArrayList<DownloadListener> tlisteners;

    private ArrayList<QueueListener> qlisteners;

    /**
     * Constructs the download manager.
     * @param c The controls to the protocol.
     */
    public DownloadManager(Controls c) {
        controls = c;

        transfers = new ArrayList<DownloadHandler>();
        tempqueue = new LinkedBlockingQueue<QueuedItem>();
        queue = new TreeMap<User, ArrayList<IncompleteFile>>();
        filemanager = new DownloadFileManager(c);
        secondqueue = new SecondQueueParse(this);
        tlisteners = new ArrayList<DownloadListener>();
        qlisteners = new ArrayList<QueueListener>();

        for (IncompleteFile f : filemanager.getIncompleteFiles()) {
            for (User u: f.getSources()) {
                if (!queue.containsKey(u)) {
                    queue.put(u, new ArrayList<IncompleteFile>());
                }
                queue.get(u).add(f);
            }
            secondqueue.directRequest(f);
        }

        firstqueuethread = controls.getTasks().backgroundTask(this);
        controls.getTasks().backgroundTask(secondqueue);
    }

    /**
     * Handle a response from a user.
     * @param f The response.
     * @param u The user.
     */
    public void handleResponse(FileResponse f, User u) {
        handle: {
            DownloadHandler h = null;
            search: {
                for (DownloadHandler i : transfers) {
                    if (i.canHandle(u, f)) {
                        h = i;
                        break search;
                    }
                }
                Controls.getLogger().finer("Could not handle response");
                break handle;
            }
            h.handle(u, f);
        }
        Controls.getLogger().finer("Received response.");
    }

    /**
     * Get all known transfers.
     */
    public List<DownloadHandler> getTransfers() {
        return transfers;
    }

    public Set<User> getQueuedUsers() {
        return queue.keySet();
    }
    
    public List<IncompleteFile> getQueueFromUser(User u) {
        if (queue.containsKey(u)) {
            return queue.get(u);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Close the manager.
     */
    public void close() {
        secondqueue.close();
        firstqueuethread.interrupt();
        for (DownloadHandler t : transfers) {
            t.close();
        }
        filemanager.close();
    }

    /**
     * Enqueues a file to a target directory.
     * @param f The file to enqueue.
     * @param u The user where the file resides.
     * @param todir The directory to download to.
     * @throws IllegalArgumentException Thrown if specified file is not a
     * directory.
     */
    public void enqueue(RemoteFile f, User u, File todir)
            throws IllegalArgumentException {
        if (todir.isDirectory()) {
            tempqueue.add(new QueuedItem(f, u, todir));
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Enqueus a file to the default directory.
     * @param f The
     * @param u The user to download from.
     */
    public void enqueue(RemoteFile f, User u) {
        enqueue(f, u, controls.getSettings().getDownloadSettings()
                .getDownloadDirectory());
    }

    /**
     * Runs the first time checking of files.
     * @param c The controls of the protocol.
     */
    public void runTask(Controls c) {
        run: {
            while (true) {
                try {
                    QueuedItem i = tempqueue.take();
                    if (i.getFile().isDirectory()) {
                        // TODO recurse
                    } else {
                        // is file
                        File f = i.getTargetFile();

                        IncompleteFile inc;
                        if (filemanager.fileExists(f)) {
                            inc = filemanager.getFile(f);
                        } else {
                            inc = filemanager.newFile(f, i.getFile().getSize());
                        }

                        if (inc.matches(i.getFile())) {
                            try {
                                inc.addSource(i.getUser(), i.getFile());
                            } catch (IllegalArgumentException iae) {
                                // Ignore this
                            }
                            // If adding source fails, don't add to queue
                            if (!queue.containsKey(i.getUser())) {
                                queue.put(i.getUser(), new ArrayList<IncompleteFile>());
                            }
                            
                            if (!queue.get(i.getUser()).contains(inc)) {
                                queue.get(i.getUser()).add(inc);

                                QueueEvent e = new QueueEvent(inc, this);
                                for (QueueListener lis : qlisteners) {
                                    lis.queueAdded(e);
                                }
                            }
                        }
                    }
                } catch (InterruptedException ie) {
                    // This should occur
                    Thread.interrupted();
                    break run;
                } catch (FileNotFoundException nffe) {
                    // Ignore, shouldn't happen
                } catch (FileExistsException fee) {
                    // Ignore, we don't overwrite
                }
            }
        }
    }

    /**
     * Removes a file from the queue.
     * @param f The file to remove from the queue.
     */
    public void removeFromQueue(IncompleteFile f) {
        for (User u: queue.keySet()) {
            if (queue.get(u).contains(f)) {
                queue.get(u).remove(f);
            }
        }
        
        QueueEvent e = new QueueEvent(f, this);
        for (QueueListener lis : qlisteners) {
            lis.queueRemoved(e);
        }
    }

    public void addQueueListener(QueueListener lis) {
        qlisteners.add(lis);
    }

    public void removeQueueListener(QueueListener lis) {
        qlisteners.remove(lis);
    }

    public void addTransferListener(DownloadListener lis) {
        tlisteners.add(lis);
    }

    public void removeTransferListener(DownloadListener lis) {
        tlisteners.remove(lis);
    }

    protected void updatedFile(IncompleteFile f) {
        QueueEvent e = new QueueEvent(f, this);
        for (QueueListener lis : qlisteners) {
            lis.queueUpdated(e);
        }
    }

    protected void updatedTransfer(DownloadHandler h) {
        DownloadEvent e = new DownloadEvent(h, this);
        for (DownloadListener lis : tlisteners) {
            lis.downloadUpdated(e);
        }
    }

    /**
     * Checks if this file is the only one.
     * @param f The incomplete file to find.
     */
    protected synchronized boolean soleFile(IncompleteFile f) {
        for (DownloadHandler h : transfers) {
            if (f.equals(h.getFile())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a handler.
     * @param h The handler to add.
     */
    protected void addDownloadHandler(DownloadHandler h) {
        if (!transfers.contains(h)) {
            transfers.add(h);

            DownloadEvent e = new DownloadEvent(h, this);
            for (DownloadListener lis : tlisteners) {
                lis.downloadAdded(e);
            }
        }
    }

    /**
     * Removes a handler.
     * @param h The handler to remove.
     */
    protected void removeDownloadHandler(DownloadHandler h) {
        if (transfers.contains(h)) {
            transfers.remove(h);

            DownloadEvent e = new DownloadEvent(h, this);
            for (DownloadListener lis : tlisteners) {
                lis.downloadRemoved(e);
            }
        }
    }
}
