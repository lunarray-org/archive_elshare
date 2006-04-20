package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.state.download.DownloadSettings;

/**
 * The manager for downloads.
 * @author Pal Hargitai
 */
public class DownloadFileManager {
    /**
     * Known downloadable files.
     */
    private TreeMap<File, IncompleteFile> files;

    /**
     * Download settings.
     */
    private DownloadSettings settings;

    /**
     * Controls to the protocol.
     */
    private Controls controls;

    /**
     * Constructs the download manager. Also initialises the known files.
     * @param c The controls for the protocol.
     */
    public DownloadFileManager(Controls c) {
        controls = c;
        settings = controls.getSettings().getDownloadSettings();
        files = new TreeMap<File, IncompleteFile>();

        init();
    }

    /**
     * Gets all incomplete files.
     * @return All known files.
     */
    public Collection<IncompleteFile> getIncompleteFiles() {
        return files.values();
    }

    /**
     * Closes down all files.
     */
    public void close() {
        for (File f : files.keySet()) {
            files.get(f).close();
        }
    }

    /**
     * Get an incomplete file.
     * @param f The file to get the incomplete version of.
     * @return The file.
     * @throws FileNotFoundException Thrown if the given file is not known.
     */
    public IncompleteFile getFile(File f) throws FileNotFoundException {
        if (files.containsKey(f)) {
            return files.get(f);
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Checks wether a given file is known.
     * @param f The file to check the knowledgeof.
     * @return True if the file is known, false if the file is unknown.
     */
    public boolean fileExists(File f) {
        return files.containsKey(f);
    }

    /**
     * Make a new file with the specified data.
     * @param f The file to save at.
     * @param s The size of the file.
     * @return The new file.
     * @throws FileExistsException Thrown if the file already exists
     */
    public IncompleteFile newFile(File f, long s) throws FileExistsException {
        if (!files.containsKey(f) && !f.exists()) {
            Hash fnh = new Hash(f.getPath());

            IncompleteFileSettings e = new IncompleteFileSettings(fnh
                    .toString(), settings);

            IncompleteFile n = new IncompleteFile(e, controls);

            try {
                n.initFromFront(s, f);

                files.put(f, n);
                return n;
            } catch (IllegalStateException ifse) {
                // Shouldn't happen, but throw new exception
                throw new FileExistsException();
            }
        } else {
            throw new FileExistsException();
        }
    }

    /**
     * Initialises all files from the backend.
     */
    private void init() {
        for (String s : settings.getFileKeys()) {
            IncompleteFileSettings e = new IncompleteFileSettings(s, settings);
            IncompleteFile n = new IncompleteFile(e, controls);
            n.initFromSettings();
            files.put(e.getLocalTarget(), n);
        }
    }
}
