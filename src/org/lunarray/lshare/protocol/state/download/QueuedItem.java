package org.lunarray.lshare.protocol.state.download;

import java.io.File;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A queued item. Ready for preprocessing.
 * @author Pal Hargitai
 */
public class QueuedItem {
    /**
     * The remote entry to process.
     */
    private RemoteFile file;

    /**
     * The user it resides with.
     */
    private User user;

    /**
     * The directory that this file is to download to.
     */
    private File target;

    /**
     * Constructs a queued item. Ready for preprocessing.
     * @param f The remote entry to download.
     * @param u The user to download from.
     * @param to The directory to download to. Or if file the direct file to
     * download to.
     */
    public QueuedItem(RemoteFile f, User u, File to) {
        file = f;
        user = u;
        target = to;
    }

    /**
     * Gets the remote entry.
     * @return The remote entry.
     */
    public RemoteFile getFile() {
        return file;
    }

    /**
     * Gets the user.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the directory where this file is to download to.
     * @return The target this file is to download to.
     */
    public File getTarget() {
        return target;
    }

    /**
     * Gets the target file. This is the name of the file in the target path.
     * @return The file that this is to download to.
     */
    public File getTargetFile() {
        return target.isDirectory() ? new File(target.getPath()
                + File.separator + file.getName()) : target;
    }
}
