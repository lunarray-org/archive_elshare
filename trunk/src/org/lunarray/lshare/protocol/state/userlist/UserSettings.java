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
package org.lunarray.lshare.protocol.state.userlist;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

/**
 * The settings that a userlist object can use.
 * @author Pal Hargitai
 */
public class UserSettings {
    /**
     * The location of buddy information. This is: {@value}.
     */
    public final static String BUDDY_LOC = "/buddies";

    /**
     * The raw settings to which these settings have access.
     */
    private RawSettings rsettings;

    /**
     * Constructs the user settings.
     * @param rs The raw settings to which these settings have access.
     */
    public UserSettings(RawSettings rs) {
        rsettings = rs;
    }

    /**
     * Registers a username and challenge as a buddy.
     * @param un The username to register.
     * @param ch The challenge to register.
     */
    public void saveBuddy(String un, String ch) {
        Controls.getLogger().finer("Buddy added: " + un);
        rsettings.setString(Settings.DEFAULT_LOC + BUDDY_LOC, ch, un);
    }

    /**
     * Remove a specified challenge as a buddy.
     * @param ch The challenge to unregister.
     */
    public void removeBuddy(String ch) {
        Controls.getLogger().finer(
                "Buddy removed: "
                        + rsettings.getString(Settings.DEFAULT_LOC + BUDDY_LOC,
                                ch, Settings.USERNAME_UNSET));
        rsettings.remove(Settings.DEFAULT_LOC + BUDDY_LOC, ch);
    }

    /**
     * Gets the name associated with the
     * @param su The challenge to get the username of.
     * @return The username associated with the challenge.
     */
    public String getSavedName(String su) {
        return rsettings.getString(Settings.DEFAULT_LOC + BUDDY_LOC, su,
                Settings.CHALLENGE_UNSET);
    }

    /**
     * Get all saved challenges.
     */
    public String[] getChallenges() {
        return rsettings.getKeys(Settings.DEFAULT_LOC + BUDDY_LOC);
    }
}
