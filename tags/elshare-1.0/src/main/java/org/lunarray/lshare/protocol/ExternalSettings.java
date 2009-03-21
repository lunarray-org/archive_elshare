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

/**
 * An interface describing some standard settings available for the user
 * interface.
 * @author Pal Hargitai
 */
public interface ExternalSettings {

    /**
     * Get the username set in the protocol to identify this user to other
     * instances of this protocol.
     * @return The string that is used to identify this user.
     */
    public String getUsername();

    /**
     * Set the username in this protocol to identify this user to other
     * instances of this protocol.
     * @param un The new username.
     */
    public void setUsername(String un);

    /**
     * Gets the challenge to uniquely identify this user and identify it as a
     * buddy to other users.
     * @return The challenge set in this protocol.
     */
    public String getChallenge();

    /**
     * Sets the challenge to uniquely identify this user.
     * @param c The new challenge.
     */
    public void setChallenge(String c);

    /**
     * Gets settings available for the user interface.
     * @return The settings available for the user interface.
     */
    public GUISettings getSettingsForGUI();
    
    
    /**
     * Gets the directory where files are downloaded to.
     * @return The download directory.
     */
    public File getDownloadDir();
    
    /**
     * Sets the directory where files are downloaded to.
     * @param f The new download directory.
     */
    public void setDownloadDir(File f);
}
