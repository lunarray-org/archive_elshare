package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.settings.RawSettings;

public class DownloadSettings {
	
	/**
	 * The default location of the settings that are to be saved. This location
	 * is {@value}.
	 */
	public final static String DEFAULT_LOC = "/lshare";
	
	public final static String DOWNLOAD_LOC = "/download";
	
	public final static String INCOMPLETE_LOC = "/incomplete";
	
	public final static String DOWNLOAD_KEY = "incoming";
	
	public final static String INCOMPLETE_HASH_KEY = "hash";
	
	public final static String INCOMPLETE_SIZE_KEY = "size";
	
	public final static long INCOMPLETE_SIZE_UNSET = -1;
	
	public final static String INCOMPLETE_TARGET_KEY = "localtarget";
	
	public final static String INCOMPLETE_TARGET_UNSET = "download.tmp";
	
	public final static String INCOMPLETE_SOURCES_LOC = "/sources";
	
	public final static String INCOMPLETE_SOURCES_PATH_KEY = "path";
	
	public final static String INCOMPLETE_SOURCES_PATH_UNSET = "path";
	
	public final static String INCOMPLETE_SOURCES_NAME_KEY = "name";
	
	public final static String INCOMPLETE_CHUNKS_LOC = "/chunks";

	private RawSettings settings;
	
	public DownloadSettings(RawSettings r) {
		settings = r;
	}
	
	public String[] getFileKeys() {
		return settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC);
	}
	
	public Hash getFileHash(String k) {
		return new Hash(settings.getByteArray(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_HASH_KEY, Hash.UNSET));
	}
	
	public void setFileHash(Hash h, String k) {
		settings.setByteArray(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_HASH_KEY, h.getBytes());
	}
	
	public long getFileSize(String k) {
		return settings.getLong(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_SIZE_KEY, -1);
	}
	
	public void setFileSize(long s, String k) {
		settings.setLong(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_SIZE_KEY, s);
	}
	
	public String getFileTarget(String k) {
		return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_TARGET_KEY, INCOMPLETE_TARGET_UNSET);
	}
	
	public void setFileTarget(String t, String k) {
		settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + k, 
				INCOMPLETE_TARGET_KEY, t);
	}
	
	public String[] getChunkKeys(String k) {
		return settings.getKeys(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_CHUNKS_LOC);
	}
	
	public void clearChunkKeys(String k) {
		for (String s: getChunkKeys(k)) {
			settings.remove(DEFAULT_LOC + DOWNLOAD_LOC + k + 
					INCOMPLETE_CHUNKS_LOC, s);
		}
	}
	
	public void setChunk(String k, long begin, long end) {
		settings.setLong(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_CHUNKS_LOC, Long.valueOf(begin).toString(), end);
	}
	
	public long getChunkValue(String k, String c) {
		return settings.getLong(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_CHUNKS_LOC, c, INCOMPLETE_SIZE_UNSET);
	}
	
	public void setDownloadDirectory(String l) {
		settings.setString(DEFAULT_LOC + DOWNLOAD_LOC, DOWNLOAD_KEY, l);
	}
	
	public String getDownloadDirectory() {
		return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC, DOWNLOAD_KEY, 
				System.getProperty("user.dir"));
	}
	
	public String[] getSources(String k) {
		return settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC);
	}
	
	public String getSourcePath(String k, String c) {
		return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC + c, INCOMPLETE_SOURCES_PATH_KEY,
				INCOMPLETE_SOURCES_PATH_UNSET);
	}
	
	public String getSourceName(String k, String c) {
		return settings.getString(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC + c, INCOMPLETE_SOURCES_NAME_KEY,
				INCOMPLETE_TARGET_UNSET);
	}
	
	public void clearSources(String k) {
		for (String s: settings.getChildren(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC)) {
			settings.removeNode(DEFAULT_LOC + DOWNLOAD_LOC + k + 
					INCOMPLETE_SOURCES_LOC + s);
		}
		settings.flush();
	}
	
	public void setSource(String k, String c, String p, String n) {
		settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC + c, INCOMPLETE_SOURCES_NAME_KEY, n);
		settings.setString(DEFAULT_LOC + DOWNLOAD_LOC + k + 
				INCOMPLETE_SOURCES_LOC + c, INCOMPLETE_SOURCES_PATH_KEY, p);
	}
}
