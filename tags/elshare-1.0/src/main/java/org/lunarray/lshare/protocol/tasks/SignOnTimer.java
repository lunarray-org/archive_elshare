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
package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.user.SignOnOut;

/**
 * The signon timer for broadcasting signons regularly.
 * @author Pal Hargitai
 */
public class SignOnTimer extends TimedRunnableTask {
    /**
     * The delay between a broadcast. This is: {@value}.
     */
    public final static int DELAY = 20000;

    /**
     * Get the delay between a broadcast.
     */
    public int getDelay() {
        return DELAY;
    }

    /**
     * Run the signon broadcast.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        SignOnOut soo = new SignOnOut(c);
        c.getUDPTransport().send(soo);
    }
}
