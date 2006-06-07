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

import org.lunarray.lshare.protocol.RemoteFile;

/**
 * Some standard functions to allow an more appealing representation of the data
 * given in this protocol.
 * @author Pal Hargitai
 */
public class GUIUtil {

    /**
     * Gives a short and readable version of the file size.
     * @param n The number (size) to pretty print.
     * @return A pretty printed version of the given size.
     */
    public static String prettyPrint(long n) {
        String[] units = {
                "B", "KB", "MB", "GB"
        };
        int i = 0;
        while (n > 9999 && i < units.length) {
            n = n / 1024;
            i++;
        }
        return Long.valueOf(n).toString() + " " + units[i];
    }

    /**
     * Strips the last element off the given dir path.
     * @param path The path to strip.
     * @return The path with the last path element stripped.
     */
    public static String stripDirPath(String path) {
        return path.contains(RemoteFile.SEPARATOR) ? path.substring(0, path
                .lastIndexOf(RemoteFile.SEPARATOR)) : "";
    }
}
