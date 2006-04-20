package org.lunarray.lshare.protocol.state.upload;

import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.userlist.User;

// TODO slot limit
// TODO add external list
/** A manager for handling uploads.
 * @author Pal Hargitai
 */
public class UploadManager {
    /** The first port to bind to.
     */
	public final static int BEGIN_PORT = 7401;
    
    /** The last port to bind to.
     */
	public final static int END_PORT = 7500;
	
    /** All registered uploads.
     */
	private ArrayList<UploadTransfer> uploads;
    
    /** The controls to the protocol.
     */
	private Controls controls;
	
    /** Constructs an upload manager.
     * @param c The controls to the protocol.
     */
	public UploadManager(Controls c) {
		uploads = new ArrayList<UploadTransfer>();
		controls = c;
	}

    /** Closes all uploads.
     */
	public void close() {
		for (UploadTransfer t: uploads) {
			t.close();
		}
	}
	
    /** Gets a list of all uploads.
     * @return All known uploads.
     */
	public List<UploadTransfer> getUploads() {
		return uploads;
	}
	
    /** Processes a request for a filetransfer.
     * @param u The user the request originated from.
     * @param f The request for a filetransfer.
     */
	public void processRequest(User u, UploadRequest f) {
		UploadHandler h = new UploadHandler(this, f, u);
		controls.getTasks().backgroundTask(h);
	}
    
    /** Adds an upload.
     * @param t The upload to be added.
     */
    protected void addTransfer(UploadTransfer t) {
        if (!uploads.contains(t)) {
            uploads.add(t);
        }
    }
    
    /** Removes an upload.
     * @param t The upload to be removed.
     */
    protected void removeTransfer(UploadTransfer t) {
        if (uploads.contains(t)) {
            uploads.remove(t);
        }
    }
}
