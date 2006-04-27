package org.lunarray.lshare.protocol.state.download;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.events.DownloadListener;
import org.lunarray.lshare.protocol.events.QueueListener;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * The interface for the download manager.
 * @author Pal Hargitai
 */
public interface ExternalDownloadManager {
    /**
     * Get a list with all transfers.
     * @return The transfers in progress.
     */
    public List<DownloadHandler> getTransfers();

    /**
     * Gets a list of users who have queued files.
     * @return The queued files.
     */
    public Set<User> getQueuedUsers();
    
    public List<IncompleteFile> getQueueFromUser(User u);

    /**
     * Enqueues a file to a target directory.
     * @param f The file to enqueue.
     * @param u The user where the file resides.
     * @param todir The directory to download to.
     * @throws IllegalArgumentException Thrown if specified file is not a
     * directory.
     */
    public void enqueue(RemoteFile f, User u, File todir)
            throws IllegalArgumentException;

    /**
     * Enqueus a file to the default directory.
     * @param f The
     * @param u The user to download from.
     */
    public void enqueue(RemoteFile f, User u);
    
    public void addTransferListener(DownloadListener lis);
    public void removeTransferListener(DownloadListener lis);
    public void addQueueListener(QueueListener lis);
    public void removeQueueListener(QueueListener lis);
    
    public Collection<IncompleteFile> getIncompleteFiles();
}
