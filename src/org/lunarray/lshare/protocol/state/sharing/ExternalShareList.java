package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public interface ExternalShareList {
	void addShare(String name, File location);
	Set<String> getShareNames();
	SharedDirectory getShareByName(String name);
	Collection<SharedDirectory> getShares();
	void removeShare(String name);
}
