package org.lunarray.lshare.gui.search;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.protocol.events.SearchEvent;
import org.lunarray.lshare.protocol.events.SearchListener;

public class SearchList extends GUIFrame implements SearchListener {

	private SearchFilter filter;
	private LShare lshare;
	private JTable restable;
	private SearchModel model;
	
	public SearchList(LShare ls, SearchFilter f) {
		filter = f;
		lshare = ls;
		
		lshare.getSearchList().addListener(this);
		
		model = new SearchModel();
		restable = new JTable(model);
		JScrollPane sp = new JScrollPane(restable);
		
		frame.addInternalFrameListener(this);
		frame.add(sp);
		frame.setTitle(getTitle());
	}
	
	public String getTitle() {
		return filter.getName();
	}
	
	public void searchResult(SearchEvent e) {
		if (filter.isValid(e)) {
			model.processEvent(e);
		}
	}
	
	@Override
	public void close() {
		lshare.getSearchList().removeListener(this);
	}
}
