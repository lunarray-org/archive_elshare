package org.lunarray.lshare.protocol.state.upload;

import org.lunarray.lshare.protocol.settings.RawSettings;

/**
 * The settings for uploads.
 * @author Pal Hargitai
 */
public class UploadSettings {
    /**
     * The default location of the settings that are to be saved. This location
     * is {@value}.
     */
    public final static String DEFAULT_LOC = "/lshare";

    /**
     * The default location for upload settings. This is {@value}.
     */
    public final static String UPLOAD_LOC = "/upload";

    /**
     * The default key for the amount of available upload slots. This is
     * {@value}.
     */
    public final static String SLOT_KEY = "slots";

    /**
     * The default amount of slots. This is {@value}.
     */
    public final static int SLOT_UNSET = 5;

    /**
     * The default key for the upload rate. This is {@value}.
     */
    public final static String UP_KEY = "uprate";

    /**
     * The default upload rate. This is {@value}.
     */
    public final static int UP_UNSET = 1000;

    /**
     * The raw settings used to fetch settings.
     */
    private RawSettings rsettings;

    /**
     * Constructs the upload settings.
     * @param r The raw settings to access.
     */
    public UploadSettings(RawSettings r) {
        rsettings = r;
    }

    /**
     * Get the amount of set slots.
     * @return The amount of set slots.
     */
    public int getSlots() {
        return rsettings.getInt(DEFAULT_LOC + UPLOAD_LOC, SLOT_KEY, SLOT_UNSET);
    }

    /**
     * Set the amount of slots.
     * @param slots The new amount of available slots.
     */
    public void setSlots(int slots) {
        rsettings.setInt(DEFAULT_LOC + UPLOAD_LOC, SLOT_KEY, slots);
    }

    /**
     * Get the maximum upload rate.
     * @return The upload rate.
     */
    public int getUpRate() {
        return rsettings.getInt(DEFAULT_LOC + UPLOAD_LOC, UP_KEY, UP_UNSET);
    }

    /**
     * Set the upload rate.
     * @param rate The new upload rate.
     */
    public void setUpRate(int rate) {
        rsettings.setInt(DEFAULT_LOC + UPLOAD_LOC, UP_KEY, rate);
    }
}
