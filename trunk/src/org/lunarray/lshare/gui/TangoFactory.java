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
package org.lunarray.lshare.gui;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;

/**
 * A factory that allows for a slightly easier access to tango, or any other
 * icon resource. 
 * @author Pal Hargitai
 */
public class TangoFactory {

    /**
     * The icons that may be accessed.
     */
    private static Map<String, ImageIcon> icons = new TreeMap<String, ImageIcon>();
    
    /**
     * Get a specific icon.
     * @param s The icon string to get.
     * @return The icon.
     */
    public static ImageIcon getIcon(String s) {
        if (!icons.containsKey(s)) {
            String sloc = "content/icons/" + s + ".png";
            URL u = ClassLoader.getSystemResource(sloc);
            icons.put(s, new ImageIcon(u));
        }
        return icons.get(s);
    }
    
}
