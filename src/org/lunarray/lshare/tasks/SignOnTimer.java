package org.lunarray.lshare.tasks;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.SignOnOut;

public class SignOnTimer extends TimedRunnableTask {

	public static int DELAY = 20000;
	
	public int getDelay() {
		return DELAY;
	}
	
	public void runTask(Controls c) {
		SignOnOut soo = new SignOnOut(c);
		c.getUDPTransport().send(soo.getPacket());
	}

}
