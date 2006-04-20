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
