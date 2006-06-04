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
package org.lunarray.lshare.protocol.settings;

import org.lunarray.lshare.protocol.Settings;

/**
 * The user interface settings.
 * @author Pal Hargitai
 */
public class GUISettings {

    /**
     * The default location for userinterface settings. Usually {@value}.
     */
    public final static String DEFAULT_LOC = Settings.DEFAULT_LOC + "/gui";

    /**
     * The raw settings to write the data to.
     */
    private RawSettings rs;

    /**
     * Constructs the user interface settings.
     * @param r The raw settings.
     */
    public GUISettings(RawSettings r) {
        rs = r;
    }

    /**
     * Remove the key and value at the given location.
     * @param loc The location.
     * @param key They key.
     */
    public void remove(String loc, String key) {
        rs.remove(DEFAULT_LOC + loc, key);
    }

    /**
     * Removes the node, and it's child nodes, at the given location.
     * @param loc The location.
     */
    public void removeNode(String loc) {
        rs.removeNode(DEFAULT_LOC + loc);
    }

    /**
     * Get the keys of the node.
     * @param loc The location of the node.
     * @return The keys of the node.
     */
    public String[] getKeys(String loc) {
        return rs.getKeys(DEFAULT_LOC + loc);
    }

    /**
     * Get a value specified by a given key.
     * @param loc The location of the node.
     * @param key The key to get value of.
     * @param def The default value if key doesn't exist.
     * @return The value associated with the key, or the default value.
     */
    public String getString(String loc, String key, String def) {
        return rs.getString(DEFAULT_LOC + loc, key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setString(String loc, String key, String val) {
        rs.setString(DEFAULT_LOC + loc, key, val);
    }

    /**
     * Get the int at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public int getInt(String loc, String key, int def) {
        return rs.getInt(DEFAULT_LOC + loc, key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setInt(String loc, String key, int val) {
        rs.setInt(DEFAULT_LOC + loc, key, val);
    }

    /**
     * Get the bool at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public boolean getBool(String loc, String key, boolean def) {
        return rs.getBool(DEFAULT_LOC + loc, key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setBool(String loc, String key, boolean val) {
        rs.setBool(DEFAULT_LOC + loc, key, val);
    }

    /**
     * Get the bytearray at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public byte[] getByteArray(String loc, String key, byte[] def) {
        return rs.getByteArray(DEFAULT_LOC + loc, key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setByteArray(String loc, String key, byte[] val) {
        rs.setByteArray(DEFAULT_LOC + loc, key, val);
    }

    /**
     * Get the at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public long getLong(String loc, String key, long def) {
        return rs.getLong(DEFAULT_LOC + loc, key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setLong(String loc, String key, long val) {
        rs.setLong(DEFAULT_LOC + loc, key, val);
    }
}
