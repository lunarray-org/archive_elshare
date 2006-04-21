package org.lunarray.lshare.protocol.state.upload;

import java.util.List;

/**
 * An external interface for upload management.
 * @author Pal Hargitai
 */
public interface ExternalUploadManager {
    /**
     * Sets the amount of available upload slots.
     * @param s The new amount of upload slots.
     */
    public void setSlots(int s);
    
    /**
     * Gets the amount of available upload slots.
     * @return The amount of upload slots.
     */
    public int getSlots();
    
    /**
     * Set the download rate.
     * @param r The new download rate.
     */
    public void setRate(int r);
    
    /**
     * Get the download rate.
     * @return The download rate.
     */
    public int getRate();
    
    /**
     * Gets a list of all uploads.
     * @return All known uploads.
     */
    public List<UploadTransfer> getUploads();
}
