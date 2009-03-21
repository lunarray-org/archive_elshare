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
package org.lunarray.lshare.gui.main;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.gui.TangoFactory;

public class About extends GUIFrame {

    public About(MainGUI mg, LShare ls) {
        super(mg, ls);
        
        JTextArea ta = new JTextArea();
        JScrollPane js = new JScrollPane(ta);
        ta.setEditable(false);
        ta.setText("eLShare allows you to share.\n" +
"Copyright (C) 2006 Pal Hargitai\n" +
"E-Mail: pal@lunarray.org\n" +
"\n" +
"This program is free software; you can redistribute it and/or\n" +
"modify it under the terms of the GNU General Public License\n" +
"as published by the Free Software Foundation; either version 2\n" +
"of the License, or any later version.\n" +
"\n" +
"This program is distributed in the hope that it will be useful,\n" +
"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
"GNU General Public License for more details.\n" +
"\n" +
"You should have received a copy of the GNU General Public License\n" +
"along with this program; if not, write to the Free Software\n" +
"Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.\n" +
"-----------------------------------------------------------------------------\n" +
"All icons used in this software are subject to the following license:\n" +
"\n" +
"The Tango icons are licensed under the Creative Commons Attribution Share-Alike\n" +
"license. The palette is public domain, it can be used and distributed freely.\n" +
"Developers, feel free to ship it along with your application. The icon naming\n" +
"utilities are licensed under the GPL.\n" +
"\n" +
"The full version of the license may be found here:\n" +
"http://creativecommons.org/licenses/by-sa/2.5/");
        frame.add(js);
        frame.setTitle("About");
        frame.setVisible(true);
        frame.setFrameIcon(TangoFactory.getIcon("help-browser"));
    }
    
    @Override
    public void close() {
        // Nothing
        frame.setVisible(false);
    }
}
