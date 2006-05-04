package org.lunarray.lshare.gui.search;

import org.lunarray.lshare.protocol.events.SearchEvent;

/**
 * A regular filter that searches a result for a matching string.
 * @author Pal Hargitai
 */
public class StringFilter implements SearchFilter {
    /**
     * The string to filter on.
     */
    private String filter;

    /**
     * Constructs a filter with a specified string.
     * @param f The string to filter on.
     */
    public StringFilter(String f) {
        filter = f;
    }

    /**
     * Checks wether a search result is valid to be processed.
     * @param res The result to check.
     * @return True if the name of the entry in the result contains the
     * specified search string.
     */
    public boolean isValid(SearchEvent res) {
        return res.getEntry().getName().contains(filter);
    }

    /**
     * Gets the name of this filter.
     * @return The name of this filter, generally the search string.
     */
    public String getName() {
        return filter;
    }
}
