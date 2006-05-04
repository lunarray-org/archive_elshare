package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;

import org.lunarray.lshare.protocol.Hash;

/**
 * A share entry.
 * @author Pal Hargitai
 */
public class ShareEntry {
    /**
     * The file in the share entry.
     */
    private File file;

    /**
     * The path in the entry.
     */
    private String path;

    /**
     * The name of the entry.
     */
    private String name;

    /**
     * The settings that are to be accessed.
     */
    private ShareSettings settings;

    /**
     * Constructs a share entry.
     * @param f The file represented in the entry.
     * @param n The name of the entry.
     * @param p The path in which the entry resides.
     * @param s The settings that are to be accessed.
     */
    public ShareEntry(File f, String n, String p, ShareSettings s) {
        file = f;
        path = p;
        name = n;
        settings = s;
    }

    /**
     * Checks wether the given entry is a file.
     * @return True if the entry is a file, false if not.
     */
    public boolean isFile() {
        return file.isFile();
    }

    /**
     * Checks wether the given entry is a directory.
     * @return True if the entry is a directory, false if not.
     */
    public boolean isDirectory() {
        return !isFile();
    }

    /**
     * Gets the name of the entry.
     * @return The name of the entry.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the path of the entry.
     * @return The path of the entry.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the last modified date of the entry.
     * @return The last modified date of the entry.
     */
    public long getLastModified() {
        if (file.exists()) {
            return file.isFile() ? file.lastModified() : -1;
        } else {
            return 0;
        }
    }

    /**
     * Gets the size of the entry.
     * @return The size of the entry.
     */
    public long getSize() {
        if (file.exists()) {
            return file.isFile() ? file.length() : -1;
        } else {
            return 0;
        }
    }

    /**
     * Gets the hash of the entry.
     * @return The hash of the entry.
     */
    public Hash getHash() {
        return file.isFile() ? settings.getHash(file.getPath()) : Hash
                .getUnset();
    }

    /**
     * Gets the file represented in this entry.
     * @return The file.
     */
    public File getFile() {
        return file;
    }
}