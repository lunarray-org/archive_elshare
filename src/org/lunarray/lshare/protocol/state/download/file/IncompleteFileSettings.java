package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.state.download.DownloadSettings;

public class IncompleteFileSettings {

    private String key;

    private DownloadSettings settings;

    public IncompleteFileSettings(String k, DownloadSettings s) {
        settings = s;
        key = k;
    }

    public void removeFile() {
        settings.removeFile(key);
    }

    public long getSize() {
        return settings.getFileSize(key);
    }

    public Hash getHash() {
        return settings.getFileHash(key);
    }

    public File getLocalTarget() {
        return new File(settings.getFileTarget(key));
    }

    public void setHash(Hash h) {
        settings.setFileHash(h, key);
    }

    public void setSize(long s) {
        settings.setFileSize(s, key);
    }

    public void setLocalTarget(File f) {
        settings.setFileTarget(f.getPath(), key);
    }

    public void setChunk(long begin, long end) {
        settings.setChunk(key, begin, end);
    }

    public void clearChunks() {
        settings.clearChunkKeys(key);
    }

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

    public String[] getSources() {
        return settings.getSources(key);
    }

    public String getSourcePath(String c) {
        return settings.getSourcePath(key, c);
    }

    public String getSourceName(String c) {
        return settings.getSourceName(key, c);
    }

    public void clearSources() {
        settings.clearSources(key);
    }

    public void setSource(String c, String p, String n) {
        settings.setSource(key, c, p, n);
    }
}
