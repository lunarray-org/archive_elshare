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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.lunarray.lshare.protocol.Controls;

/**
 * The raw settings to be used by the other settings.
 * @author Pal Hargitai
 */
public class RawSettings {
    /**
     * The java preferences to use.
     */
    private Preferences prefs;

    /**
     * The constructor, gets the settings for the package.
     */
    public RawSettings() {
        prefs = Preferences.userNodeForPackage(getClass());
    }

    /**
     * Handles quitting, flushes settings to backstore.
     */
    public void quit() {
        try {
            prefs.flush();
        } catch (BackingStoreException bse) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Remove the key and value at the given location.
     * @param loc The location.
     * @param key They key.
     */
    public void remove(String loc, String key) {
        Preferences n = prefs.node(loc);
        n.remove(key);
    }

    /**
     * Removes the node, and it's child nodes, at the given location.
     * @param loc The location.
     */
    public void removeNode(String loc) {
        Preferences n = prefs.node(loc);
        try {
            n.removeNode();
        } catch (BackingStoreException bse) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Get the keys of the node.
     * @param loc The location of the node.
     * @return The keys of the node.
     */
    public String[] getKeys(String loc) {
        try {
            if (prefs.nodeExists(loc)) {
                Preferences n = prefs.node(loc);
                return n.keys();
            }
        } catch (BackingStoreException bse) {
            Controls.getLogger().warning("Could not save settings");
        }
        return new String[0];
    }

    /**
     * Get a value specified by a given key.
     * @param loc The location of the node.
     * @param key The key to get value of.
     * @param def The default value if key doesn't exist.
     * @return The value associated with the key, or the default value.
     */
    public String getString(String loc, String key, String def) {
        Preferences n = prefs.node(loc);
        return n.get(key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setString(String loc, String key, String val) {
        Preferences n = prefs.node(loc);
        n.put(key, val);
        try {
            n.sync();
        } catch (Exception e) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Get the int at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public int getInt(String loc, String key, int def) {
        Preferences n = prefs.node(loc);
        return n.getInt(key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setInt(String loc, String key, int val) {
        Preferences n = prefs.node(loc);
        n.putInt(key, val);
        try {
            n.sync();
        } catch (Exception e) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Get the bool at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public boolean getBool(String loc, String key, boolean def) {
        Preferences n = prefs.node(loc);
        return n.getBoolean(key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setBool(String loc, String key, boolean val) {
        Preferences n = prefs.node(loc);
        n.putBoolean(key, val);
        try {
            n.sync();
        } catch (Exception e) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Get the bytearray at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public byte[] getByteArray(String loc, String key, byte[] def) {
        Preferences n = prefs.node(loc);
        return n.getByteArray(key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setByteArray(String loc, String key, byte[] val) {
        Preferences n = prefs.node(loc);
        n.putByteArray(key, val);
        try {
            n.sync();
        } catch (Exception e) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Get the long at the given key.
     * @param loc The location of the node.
     * @param key The key to get the value of;
     * @param def The default value if none is set.
     * @return The value associated with the key, or default.
     */
    public long getLong(String loc, String key, long def) {
        Preferences n = prefs.node(loc);
        return n.getLong(key, def);
    }

    /**
     * Associate a value to a given key.
     * @param loc The location of the node.
     * @param key The key to associate.
     * @param val The value.
     */
    public void setLong(String loc, String key, long val) {
        Preferences n = prefs.node(loc);
        n.putLong(key, val);
        try {
            n.sync();
        } catch (Exception e) {
            Controls.getLogger().warning("Could not save settings");
        }
    }

    /**
     * Gets the names of all children.
     * @param loc The location to get the children of.
     * @return The children of the specified location.
     */
    public String[] getChildren(String loc) {
        Preferences n = prefs.node(loc);
        try {
            return n.childrenNames();
        } catch (BackingStoreException bse) {
            return new String[0];
        }
    }

    /**
     * Flush the settings to the backing store.
     */
    public void flush() {
        try {
            prefs.flush();
        } catch (BackingStoreException bse) {
            Controls.getLogger().warning("Could not save settings");
        }
    }
}
