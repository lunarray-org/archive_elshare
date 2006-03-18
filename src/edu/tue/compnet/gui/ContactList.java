package edu.tue.compnet.gui;

import java.awt.event.*;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import edu.tue.compnet.Backend;
import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.state.*;

/**
 * This panel will hold the main contact list. It will be in a tree form so
 * that it may be expandable to hold 'buddies' and regular users. It remains
 * to be seen wether this should be IP based or nickname based.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class ContactList {
	// The tree holding the users
	JTree contactlist;
	// The tree model
	DefaultTreeModel model;
	// The node
	DefaultMutableTreeNode onlinemisccontacts;
	DefaultMutableTreeNode onlinebuddies;
	DefaultMutableTreeNode offlinebuddies;
	// The selected node
	DefaultMutableTreeNode selected;
	// The backend
	Backend backend;
	// The popup menu
	ContactPopup popup;
	// The main frame
	MainFrame mainf;
	
	/**
	 * The constructor
	 * @param b The backend
	 * @param m The main frame this list resides in.
	 */
	public ContactList(Backend b, MainFrame m) {
		mainf = m;
		// Set the nodes
		onlinemisccontacts = new DefaultMutableTreeNode();
		onlinemisccontacts.setUserObject("Online users");
		onlinebuddies = new DefaultMutableTreeNode();
		onlinebuddies.setUserObject("Online Buddies");
		offlinebuddies = new DefaultMutableTreeNode();
		offlinebuddies.setUserObject("Offline Buddies");
		// Set root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.setUserObject("Contacts");
		root.add(onlinebuddies);
		root.add(onlinemisccontacts);
		root.add(offlinebuddies);
		
		model = new DefaultTreeModel(root);
		contactlist = new JTree(model);
		// Set some settings
		contactlist.setRootVisible(false);
		// Popup menu
		popup = new ContactPopup();
		contactlist.setComponentPopupMenu(popup.getMenu());
		// The selection
		contactlist.getSelectionModel().setSelectionMode(TreeSelectionModel.
				SINGLE_TREE_SELECTION);
		contactlist.addTreeSelectionListener(new TreeSelectionListener() {
			/*
			 * Handle selections.
			 */
			public void valueChanged(TreeSelectionEvent arg0) {
				selected = (DefaultMutableTreeNode)contactlist.
						getLastSelectedPathComponent();
				if (selected != null && selected != onlinebuddies &&
						selected != offlinebuddies &&
						selected != onlinemisccontacts) {
					String[] title = new String[3];
					User u = (User)selected.getUserObject();
					// Set some info
					title[0] = u.getName();
					title[1] = u.getAddress();
					boolean isbuddy = selected.getParent() !=
							onlinemisccontacts;
					popup.setContact(new ActionListener() {
						/*
						 * Incase a selected user is a buddy, remove from
						 * buddy list, otherwise add to buddy list. 
						 */
						public void actionPerformed(ActionEvent arg0) {
							if (selected != null && selected !=
									onlinebuddies && selected != offlinebuddies
									&& selected != onlinemisccontacts) {
								// It's a regular node
								User u = (User)selected.getUserObject();
								if (backend.getState().getBuddyList().isBuddy(
										u.getAddress())) {
									removeFromList(u, offlinebuddies);
									removeFromList(u, onlinebuddies);
									insertIntoList(u, onlinemisccontacts);
									backend.getState().getBuddyList().
										removeBuddy(u.getAddress());
								} else {
									removeFromList(u, onlinemisccontacts);
									insertIntoList(u, onlinebuddies);
									backend.getState().getBuddyList().
											addBuddy(u.getName(), u.
											getAddress());
								}
								//populateList();
								showAll();
							}
						}
					}, new ActionListener() {
						/*
						 * Request user list
						 */
						public void actionPerformed(ActionEvent arg0) {
							if (selected != null && selected !=
									onlinebuddies && selected != offlinebuddies
									&& selected != onlinemisccontacts) {
								// It's a regular node
								User u = (User)selected.getUserObject();
								backend.getState().getTasks().requestFilelist(
										u.getAddress());
								UserFilter f = new UserFilter(u.getName(), u.
										getAddress());
								mainf.search(f);
							}
						}
					}, title, isbuddy);
				}
			}
		});
		
		backend = b;
		// Register the listener
		backend.getState().getUserList().addUserListListener(
				new BackListen());
	}
	
	/**
	 * Show all nodes in the list
	 */
	private void showAll() {
		// Show all
		model.reload();
		try {
			for (int i = 0; i < contactlist.getRowCount(); i++) {
				contactlist.expandRow(i);
			}
		} catch (ArrayIndexOutOfBoundsException ie) {
			// Ignore this
		}
	}
	
	/**
	 * The popup menu of the contact list.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class ContactPopup {
		// The menu		
		JPopupMenu menu;
		
		
		/**
		 * The constructor for the popup menu.
		 */
		public ContactPopup() {
			menu = new JPopupMenu();
			menu.setLabel("No selection made");
		}
		
		/**
		 * Get the popup menu.
		 * @return The menu.
		 */
		public JPopupMenu getMenu() {
			return menu;
		}
		
		/**
		 * Ensure that some actions can occur.
		 * @param a The action listener for the add/remove buddy
		 * @param b The action listener for the show userlist
		 * @param title The title.
		 * @param isbuddy Wether the user is a buddy.
		 */
		public void setContact(ActionListener a, ActionListener b,
				String[] title, boolean isbuddy) {
			menu.removeAll();
			for (String t: title) {
				menu.add(t);
			}
			menu.addSeparator();
			// Buddylist
			JMenuItem me;
			if (isbuddy) {
				me = new JMenuItem("Remove from buddylist");
			} else {
				me = new JMenuItem("Add to buddylist");
			}
			menu.add(me);
			me.addActionListener(a);
			// Userlist
			JMenuItem ul = new JMenuItem("Show userlist");
			menu.add(ul);
			ul.addActionListener(b);
			
		}
	}
	
	/**
	 * The listener for the backend.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class BackListen implements UserListListener {
		/**
		 * An item is being added to the list
		 */
		public void listAddEvent(ListEvent e) {
			// Make a new user and use standard function
			ListUser lu = e.getTriggerUser();
			User u = new User(lu.getName(), lu.getAddress().getHostName());
			if (e.getState().getBuddyList().isBuddy(u.getAddress())) {
				removeFromList(u, offlinebuddies);
				insertIntoList(u, onlinebuddies);
			} else {
				insertIntoList(u, onlinemisccontacts);
			}
			//populateList();
			showAll();
		}
		
		/**
		 * An item is being removed from the list
		 */
		public void listRemoveEvent(ListEvent e) {
			ListUser lu = e.getTriggerUser();
			User u = new User(lu.getName(), lu.getAddress().getHostName());
			if (e.getState().getBuddyList().isBuddy(u.getAddress())) {
				removeFromList(u, onlinebuddies);
				insertIntoList(u, offlinebuddies);
			} else {
				removeFromList(u, onlinemisccontacts);
			}
			//populateList();
			showAll();
		}
		
		/**
		 * An item in the list is being updated
		 */
		public void listUpdateEvent(ListEvent e) {
			// Just look through all users and see if the address matches
			ListUser lu = e.getTriggerUser();
			User u = new User(lu.getName(), lu.getAddress().getHostName());
			if (e.getState().getBuddyList().isBuddy(u.getAddress())) {
				removeFromList(u, onlinebuddies);
				insertIntoList(u, onlinebuddies);
			} else {
				removeFromList(u, onlinemisccontacts);
				insertIntoList(u, onlinemisccontacts);				
			}
			//populateList();
			showAll();
		}
	}
	
	/**
	 * Remove a user from the list, by address.
	 * @param ou The user to be removed, is comparing.
	 * @param l The node to remove the user from.
	 */
	public void removeFromList(User ou, DefaultMutableTreeNode l) {
		// if he has enough children, search them
		for (int i = 0; i < l.getChildCount(); i++) {
			User u = (User)((DefaultMutableTreeNode)l.getChildAt(i)).
					getUserObject();
			if (u.getAddress().equals(ou.getAddress())) {
				l.remove(i);
			}
		}
	}
	
	/**
	 * Inserts an item into the list, in a sorted manner.
	 * @param nu The user to be inserted.
	 * @param l The node to insert user in to.
	 */
	public void insertIntoList(User nu, DefaultMutableTreeNode l) {
		DefaultMutableTreeNode lun = new DefaultMutableTreeNode();
		lun.setUserObject(nu);
		synchronized (l) {
			if (l.getChildCount() > 0) {
				// if he has enough children, search them
				int i = 0;
				// parse through children
				User u = (User)((DefaultMutableTreeNode)l.getChildAt(i)).
						getUserObject();
				while ((i < l.getChildCount()) && u.getName().
						compareTo(nu.getName()) < 0) {
					u = (User)((DefaultMutableTreeNode)l.getChildAt(i)).
							getUserObject();
					i++;
				}
				if (!u.getAddress().equals(nu.getAddress())) {
					l.insert(lun, i);
				}
			} else {
				// else just add
				l.add(lun);
			}
		}
	}
	
	/**
	 * Populate the contactlist.
	 */
	public void populateList() {
		// Remove all nodes
		onlinemisccontacts.removeAllChildren();
		onlinebuddies.removeAllChildren();
		offlinebuddies.removeAllChildren();
		// The offline users
		Map<String, String> buds = backend.getState().getBuddyList().
				getBuddies();
		for (ListUser lu: backend.getState().getUserList().getUserList()) {
			if (buds.containsKey(lu.getAddress().getHostName())) {
				insertIntoList(new User(lu.getName(), lu.getAddress().
						getHostName()), onlinebuddies);
				buds.remove(lu.getAddress().getHostName());
			} else {
				insertIntoList(new User(lu.getName(), lu.getAddress().
						getHostName()), onlinemisccontacts);
			}
		}
		for (String keys: buds.keySet()) {
			insertIntoList(new User(buds.get(keys), keys), offlinebuddies);
		}
		showAll();
	}
	
	/**
	 * Get the contact list.
	 * @return The contact list
	 */
	public JTree getTree() {
		return contactlist;
	}
	
	/**
	 * This class holds the user info in the frontend
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class User {
		// The name
		String name;
		// The address
		String address;
		
		/**
		 * The constructor of a user.
		 * @param n The name of the user.
		 * @param a The address of the user.
		 */
		public User(String n, String a) {
			name = n;
			address = a;
		}
		
		/**
		 * Set the name of the user.
		 * @param n The name to set it to.
		 */
		public void setName(String n) {
			name = n;
		}
		
		/**
		 * Get the name of the user.
		 * @return The name of the user.
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Get the address of the user.
		 * @return The address of the user.
		 */
		public String getAddress() {
			return address;
		}
		
		/**
		 * Makes a pretty print of the user information.
		 */
		public String toString() {
			return name + " (" + address + ")";
		}
	}
}
