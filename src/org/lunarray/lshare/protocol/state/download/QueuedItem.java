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
    private File targetdir;

    /**
     * Constructs a queued item. Ready for preprocessing.
     * @param f The remote entry to download.
     * @param u The user to download from.
     * @param todir The directory to download to.
     */
    public QueuedItem(RemoteFile f, User u, File todir) {
        file = f;
        user = u;
        targetdir = todir;
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
     * @return The directory this file is to download to.
     */
    public File getTargetDir() {
        return targetdir;
    }

    /**
     * Gets the target file. This is the name of the file in the target path.
     * @return The file that this is to download to.
     */
    public File getTargetFile() {
        return new File(targetdir.getPath() + File.separator + file.getName());
    }
}
