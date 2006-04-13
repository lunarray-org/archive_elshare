package org.lunarray.lshare.protocol.state.download;

import java.io.File;

public class DownloadSettings {

	public File getDownloadDirectory() {
		return new File(System.getProperty("user.home"));
	}	
}
