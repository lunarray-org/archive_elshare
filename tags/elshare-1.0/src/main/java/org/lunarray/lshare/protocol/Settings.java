/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.protocol;

import java.io.File;

import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.settings.RawSettings;
import org.lunarray.lshare.protocol.state.download.DownloadSettings;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.upload.UploadSettings;
import org.lunarray.lshare.protocol.state.userlist.UserSettings;

/**
 * The basic settings for the protocol.
 * @author Pal Hargitai
 */
public class Settings implements ExternalSettings {

    /**
     * The default location of the settings that are to be saved. This location
     * is {@value}.
     */
    public final static String DEFAULT_LOC = "/lshare";

    /**
     * The key used for retrieving the username. The key for the username
     * {@value}.
     */
    public final static String USERNAME_KEY = "username";

    /**
     * The username if it's unset. This is {@value}.
     */
    public final static String USERNAME_UNSET = "anonymous";

    /**
     * The key used for retrieving the challenge. The key for the challenge is
     * {@value}.
     */
    public final static String CHALLENGE_KEY = "challenge";

    /**
     * The challenge if it's unset. The is {@value}.
     */
    public final static String CHALLENGE_UNSET = "";

    /**
     * Raw settings to be used as a backend for settings.
     */
    private RawSettings rsettings;

    /**
     * The username currently set in the system.
     */
    private String username;

    /**
     * The challenge currently set in the system.
     */
    private String challenge;

    /**
     * The settings for user list management.
     */
    private UserSettings usettings;

    /**
     * The settings for share management.
     */
    private ShareSettings ssettings;

    /**
     * The settings for download management.
     */
    private DownloadSettings dsettings;

    /**
     * The settings for upload management.
     */
    private UploadSettings lsettings;

    /**
     * Constructs the settings and all settings that are dependant on it.
     * @param c The protocol controls.
     */
    public Settings(Controls c) {
        rsettings = new RawSettings();
        usettings = new UserSettings(rsettings);
        ssettings = new ShareSettings(rsettings);
        dsettings = new DownloadSettings(rsettings);
        lsettings = new UploadSettings(rsettings);
        initSettings();
    }

    /**
     * Gets the username currently set in the system.
     * @return The username currently set.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username in the system.
     * @param un The username to set to.
     */
    public void setUsername(String un) {
        if (un.length() > 0) {
            Controls.getLogger().finer("Set username: " + un);

            username = un;
            rsettings.setString(DEFAULT_LOC, USERNAME_KEY, username);
        }
    }

    /**
     * Gets the challenge currently set in the system.
     * @return The challenge currently set in the system.
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * Sets the challenge in the system.
     * @param c The new challenge to set in the system.
     */
    public void setChallenge(String c) {
        if (c.length() > 0) {
            Controls.getLogger().finer("Set challenge: " + c);
            challenge = c;
            rsettings.setString(DEFAULT_LOC, CHALLENGE_KEY, challenge);
        }
    }

    /**
     * Gets the settings that are relevant to userlist management.
     * @return The usersettings.
     */
    public UserSettings getUserSettings() {
        return usettings;
    }

    /**
     * Gets the settings that are relevant to shareing.
     * @return The sharesettings.
     */
    public ShareSettings getShareSettings() {
        return ssettings;
    }

    /**
     * Gets the settings that are usefull for a gui.
     * @return The gui settings.
     */
    public GUISettings getSettingsForGUI() {
        return new GUISettings(rsettings);
    }
    
    /**
     * Gets the directory where files are downloaded to.
     * @return The download directory.
     */
    public File getDownloadDir() {
        return dsettings.getDownloadDirectory();
    }
    
    /**
     * Sets the directory where files are downloaded to.
     * @param f The new download directory.
     */
    public void setDownloadDir(File f) {
        dsettings.setDownloadDirectory(f);
    }

    /**
     * Gets the settings that are relevant to download management.
     * @return The download settings.
     */
    public DownloadSettings getDownloadSettings() {
        return dsettings;
    }

    /**
     * Gets the settings that are relevant to upload management.
     * @return The upload settings.
     */
    public UploadSettings getUploadSettings() {
        return lsettings;
    }

    /**
     * Initialises the standard settings.
     */
    private void initSettings() {
        username = rsettings.getString(DEFAULT_LOC, USERNAME_KEY,
                USERNAME_UNSET);
        challenge = rsettings.getString(DEFAULT_LOC, CHALLENGE_KEY,
                CHALLENGE_UNSET);
    }
}
