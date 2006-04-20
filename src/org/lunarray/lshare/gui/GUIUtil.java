package org.lunarray.lshare.gui;

import org.lunarray.lshare.protocol.state.sharing.ShareList;

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
        if (path.contains(ShareList.SEPARATOR)) {
            return path.substring(0, path.lastIndexOf(ShareList.SEPARATOR));
        } else {
            return "";
        }
    }
}
