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
"http://creativecommons.org/licenses/by-sa/2.5/\n" +
"-----------------------------------------------------------------------------\n" +
"Part of this software is subject to the following license:\n" +
"\n" +
"Copyright 1997, 1998 Sun Microsystems, Inc. All Rights Reserved.\n" +
" Redistribution and use in source and binary forms, with or\n" +
" without modification, are permitted provided that the following\n" +
" conditions are met:\n" +
"\n" + 
" - Redistributions of source code must retain the above copyright\n" +
"   notice, this list of conditions and the following disclaimer.\n" + 
"\n" + 
" - Redistribution in binary form must reproduce the above\n" +
"   copyright notice, this list of conditions and the following\n" +
"   disclaimer in the documentation and/or other materials\n" +
"   provided with the distribution.\n" + 
"\n" + 
" Neither the name of Sun Microsystems, Inc. or the names of\n" +
" contributors may be used to endorse or promote products derived\n" +
" from this software without specific prior written permission.\n" +  
"\n" + 
" This software is provided \"AS IS,\" without a warranty of any\n" +
" kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND\n" +
" WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,\n" +
" FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY\n" +
" EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY\n" +
" DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR\n" +
" RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR\n" +
" ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE\n" +
" FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,\n" +
" SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER\n" +
" CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF\n" +
" THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS\n" +
" BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\n" +
"\n" +
" You acknowledge that this software is not designed, licensed or\n" +
" intended for use in the design, construction, operation or\n" +
" maintenance of any nuclear facility. ");
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
