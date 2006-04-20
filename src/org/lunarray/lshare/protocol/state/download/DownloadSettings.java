package org.lunarray.lshare.protocol.state.download;

import java.io.File;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.settings.RawSettings;

/**
 * Settings for downloadable files.
 * @author Pal Hargitai
 */
public class DownloadSettings {

    /**
     * The default location of the settings that are to be saved. This location
     * is {@value}.
     */
    public final static String DEFAULT_LOC = "/lshare";

    /**
     * The default location of the download settings that are to be saved. This
     * location is {@value}.
     */
    public final static String DOWNLOAD_LOC = "/download";

    /**
     * The default key for the download directory. This key is {@value}.
     */
    public final static String DOWNLOAD_KEY = "incoming";

    /**
     * The default key for the hash. This key is {@value}.
     */
    public final static String INCOMPLETE_HASH_KEY = "hash";

    /**
     * The default key for the size. This key is {@value}.
     */
    public final static String INCOMPLETE_SIZE_KEY = "size";

    /**
     * The default value if the size is unset. This is {@value}.
     */
    public final static long INCOMPLETE_SIZE_UNSET = -1;

    /**
     * The default key for the target file. This key is {@value}.
     */
    public final static String INCOMPLETE_TARGET_KEY = "localtarget";

    /**
     * The default value if the target file is unset. This is {@value}.
     */
    public final static String INCOMPLETE_TARGET_UNSET = "download.tmp";

    /**
     * The default location of the source settings that are to be saved. This
     * location is {@value}.
     */
    public final static String INCOMPLETE_SOURCES_LOC = "/sources";

    /**
     * The default key for the source path. This key is {@value}.
     */
    public final static String INCOMPLETE_SOURCES_PATH_KEY = "path";

    /**
     * The default value if the path is unset. This is {@value}.
     */
    public final static String INCOMPLETE_SOURCES_PATH_UNSET = "path";

    /**
     * The default key for the source name. This key is {@value}.
     */
    public final static String INCOMPLETE_SOURCES_NAME_KEY = "name";

    /**
     * The default location of the chunk settings that are to be saved. This
     * location is {@value}.
     */
    public final static String INCOMPLETE_CHUNKS_LOC = "/chunks";

    /**
     * The raw settings to use.
     */
    private RawSettings settings;

    /**
     * Constructs the download settings.
     * @param r The raw settings to use.
     */
    public DownloadSettings(RawSettings r) {
        settings = r;
    }

    /**
     * Removes a file by the given key.
     * @param k The key of the file to remove.
     */
    public void removeFile(String k) {
        settings.removeNode(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k);
    }

    /**
     * Get the keys for all known files.
     * @return Get the keys of all known files.
     */
    public String[] getFileKeys() {
        return settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC);
    }

    /**
     * Get the hash of a specified file.
     * @param k The key of the file.
     * @return The files' hash.
     */
    public Hash getFileHash(String k) {
        return new Hash(settings.getByteArray(DEFAULT_LOC + DOWNLOAD_LOC + "/"
                + k, INCOMPLETE_HASH_KEY, Hash.UNSET));
    }

    /**
     * Set the hash of a specified file.
     * @param h The hash to set to.
     * @param k The key of the file.
     */
    public void setFileHash(Hash h, String k) {
        settings.setByteArray(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k,
                INCOMPLETE_HASH_KEY, h.getBytes());
    }

    /**
     * Get the size of a specified file.
     * @param k The key of the file.
     * @return The size of the file.
     */
    public long getFileSize(String k) {
        return settings.getLong(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k,
                INCOMPLETE_SIZE_KEY, -1);
    }

    /**
     * Set the size of a specified file.
     * @param s The new size of the file.
     * @param k The key of the file.
     */
    public void setFileSize(long s, String k) {
        settings.setLong(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k,
                INCOMPLETE_SIZE_KEY, s);
    }

    /**
     * Get the target path of a specified file.
     * @param k The key of the file.
     * @return The target path of the file.
     */
    public String getFileTarget(String k) {
        return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k,
                INCOMPLETE_TARGET_KEY, INCOMPLETE_TARGET_UNSET);
    }

    /**
     * Set the target path of a specified file.
     * @param t The new target path of the file.
     * @param k The key of the file.
     */
    public void setFileTarget(String t, String k) {
        settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k,
                INCOMPLETE_TARGET_KEY, t);
    }

    /**
     * Get the keys of all chunk of a specified file. Ie get the marks of all
     * chunks.
     * @param k The key of the file.
     * @return The keys of the chunk.
     */
    public String[] getChunkKeys(String k) {
        return settings.getKeys(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_CHUNKS_LOC);
    }

    /**
     * Clear all chunks of a specified file.
     * @param k The file to clear the chunks of.
     */
    public void clearChunkKeys(String k) {
        for (String s : getChunkKeys(k)) {
            settings.remove(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                    + INCOMPLETE_CHUNKS_LOC, s);
        }
    }

    /**
     * Set chunk data of a file.
     * @param k The key of the file to set the chunk of.
     * @param begin The mark of the chunk.
     * @param end The end of the chunk.
     */
    public void setChunk(String k, long begin, long end) {
        settings.setLong(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_CHUNKS_LOC, Long.valueOf(begin).toString(), end);
    }

    /**
     * Get the associated end of the chunk by it's mark.
     * @param k The key of the file.
     * @param c The key, mark, of the chunk.
     * @return The end of the chunk.
     */
    public long getChunkValue(String k, String c) {
        return settings.getLong(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_CHUNKS_LOC, c, INCOMPLETE_SIZE_UNSET);
    }

    /**
     * Set the download directory.
     * @param l The download directory.
     */
    public void setDownloadDirectory(String l) {
        settings.setString(DEFAULT_LOC + DOWNLOAD_LOC, DOWNLOAD_KEY, l);
    }

    /**
     * Get the download directory.
     * @return The current download directory.
     */
    public File getDownloadDirectory() {
        return new File(settings.getString(DEFAULT_LOC + DOWNLOAD_LOC,
                DOWNLOAD_KEY, System.getProperty("user.dir")));
    }

    /**
     * Get the challenges of all known sources.
     * @param k The file to get the sources of.
     * @return The challenges of the sources of the file.
     */
    public String[] getSources(String k) {
        return settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_SOURCES_LOC);
    }

    /**
     * Get the path of the given source.
     * @param k The key of the file.
     * @param c The challenge of the source
     * @return The path of the source.
     */
    public String getSourcePath(String k, String c) {
        return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_SOURCES_LOC + "/" + c,
                INCOMPLETE_SOURCES_PATH_KEY, INCOMPLETE_SOURCES_PATH_UNSET);
    }

    /**
     * Get the name of the given source.
     * @param k The key of the file.
     * @param c The challenge of the source
     * @return The name of the source.
     */
    public String getSourceName(String k, String c) {
        return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_SOURCES_LOC + "/" + c,
                INCOMPLETE_SOURCES_NAME_KEY, INCOMPLETE_TARGET_UNSET);
    }

    /**
     * Clears all the sources of the specified file.
     * @param k The key of the file to clear the sources of.
     */
    public void clearSources(String k) {
        for (String s : settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC + "/"
                + k + INCOMPLETE_SOURCES_LOC)) {
            settings.removeNode(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                    + INCOMPLETE_SOURCES_LOC + s);
        }
        settings.flush();
    }

    /**
     * Sets a source for a file.
     * @param k The key of the file.
     * @param c The challenge of the source.
     * @param p The path of the source.
     * @param n The name of the source.
     */
    public void setSource(String k, String c, String p, String n) {
        settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_SOURCES_LOC + "/" + c,
                INCOMPLETE_SOURCES_NAME_KEY, n);
        settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + "/" + k
                + INCOMPLETE_SOURCES_LOC + "/" + c,
                INCOMPLETE_SOURCES_PATH_KEY, p);
    }
}
