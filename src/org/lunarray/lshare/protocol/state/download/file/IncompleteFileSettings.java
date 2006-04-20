package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.state.download.DownloadSettings;

/**
 * Settings for handling a single incomplete file.
 * @author Pal Hargitai
 */
public class IncompleteFileSettings {
    /**
     * The key with which the file is stored.
     */
    private String key;

    /**
     * The download settings to be used.
     */
    private DownloadSettings settings;

    /**
     * Constructs the settings for an incomplete file.
     * @param k The key with which the file is stored.
     * @param s The download settings to be used.
     */
    public IncompleteFileSettings(String k, DownloadSettings s) {
        settings = s;
        key = k;
    }

    /**
     * Remove this file from the settings.
     */
    public void removeFile() {
        settings.removeFile(key);
    }

    /**
     * Get the size of the file.
     * @return The size of the file.
     */
    public long getSize() {
        return settings.getFileSize(key);
    }

    /**
     * Get the hash of the file.
     * @return The hash of the file. 
     */
    public Hash getHash() {
        return settings.getFileHash(key);
    }

    /**
     * Get the local target where the file is to be downloaded to.
     * @return The local target of the file.
     */
    public File getLocalTarget() {
        return new File(settings.getFileTarget(key));
    }

    /**
     * Set the hash of the file.
     * @param h The hash to set it to.
     */
    public void setHash(Hash h) {
        settings.setFileHash(h, key);
    }

    /**
     * Set the size of the file.
     * @param s The size to set it to.
     */
    public void setSize(long s) {
        settings.setFileSize(s, key);
    }

    /**
     * Set the local download target of the file.
     * @param f The target file to set it to.
     */
    public void setLocalTarget(File f) {
        settings.setFileTarget(f.getPath(), key);
    }

    /**
     * Set a single chunk for the file.
     * @param begin The mark at which downloading should begin.
     * @param end The mend of downloading.
     */
    public void setChunk(long begin, long end) {
        settings.setChunk(key, begin, end);
    }

    /**
     * Clear all chunks.
     */
    public void clearChunks() {
        settings.clearChunkKeys(key);
    }

    /**
     * Get all known chunks of this file.
     * @return The map of all chunks f(mark) -&gt; end
     */
    public Map<Long, Long> getChunks() {
        TreeMap<Long, Long> chunks = new TreeMap<Long, Long>();
        for (String s : settings.getChunkKeys(key)) {
            try {
                Long b = Long.parseLong(s);
                Long e = settings.getChunkValue(key, s);
                chunks.put(b, e);
            } catch (NumberFormatException nfe) {
                // Invalid key
            }
        }
        return chunks;
    }

    /**
     * Get the challenges of all sources to download from.
     * @return The challenges of all sources.
     */
    public String[] getSources() {
        return settings.getSources(key);
    }

    /**
     * Get the path of the remote entry of a source.
     * @param c The challenge of the source.
     * @return The path of the entry.
     */
    public String getSourcePath(String c) {
        return settings.getSourcePath(key, c);
    }

    /**
     * Get the name of the remote entry of a source.
     * @param c The challenge of the source.
     * @return The path of the entry.
     */
    public String getSourceName(String c) {
        return settings.getSourceName(key, c);
    }

    /**
     * Clear all sources.
     */
    public void clearSources() {
        settings.clearSources(key);
    }

    /**
     * Set a single source.
     * @param c The challenge of the source.
     * @param p The path of the source.
     * @param n The name of the source.
     */
    public void setSource(String c, String p, String n) {
        settings.setSource(key, c, p, n);
    }
}
