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
package org.lunarray.lshare;

import org.lunarray.lshare.gui.MainGUI;

/**
 * A main class that does little other than start the eLShare application.
 * @author Pal Hargitai
 */
public class Main {

    /**
     * Runs the eLShare application.
     * @param args Arguments for run.
     */
    public static void main(String[] args) {
        LShare ls = new LShare();
        MainGUI mg = new MainGUI(ls);
        mg.start();
        ls.start();
    }
}
