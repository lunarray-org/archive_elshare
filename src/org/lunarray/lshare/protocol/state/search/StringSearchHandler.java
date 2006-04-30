package org.lunarray.lshare.protocol.state.search;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.search.ResultOut;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A search string handler.
 * @author Pal Hargitai
 */
public class StringSearchHandler implements RunnableTask {
    /**
     * Where the search request came from.
     */
    private InetAddress to;

    /**
     * The search string requested.
     */
    private String query;

    /**
     * Constructs a string search handler.
     * @param t The address from which the search originated.
     * @param q The string that was searched for.
     */
    public StringSearchHandler(InetAddress t, String q) {
        to = t;
        query = q;
    }

    /**
     * Process the search request.
     * @param c The controls for the protocol.
     */
    public void runTask(Controls c) {
        for (ShareEntry e : c.getState().getShareList().getEntriesMatching(
                query)) {
            System.out.println(e.getPath());
            ResultOut o = new ResultOut(to, e);
            c.getUDPTransport().send(o);
        }
    }
}
