package edu.tue.compnet.events;

/**
 * Listens for filetransfers.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface TransferListener {
	/**
	 * Register a download with the listener 
	 * @param e The event that invoked it.
	 */
	void registerDownload(FiletransferEvent e);
	
	/**
	 * Register an upload with the listener 
	 * @param e The event that invoked it.
	 */	
	void registerUpload(FiletransferEvent e);
}
