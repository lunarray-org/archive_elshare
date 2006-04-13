package edu.tue.compnet.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.tue.compnet.protocol.state.Transfer;


/**
 * The transfer window that will show data on a file transfer.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class FiletransferWindow {
	// The panel
	JPanel main;
	// If it's a download, if !isdownload then it's an upload
	boolean isdownload;
	// The progress label
	JLabel progresslabel;
	// The progress bar
	JProgressBar progressbar;
	// The main frame
	MainFrame frame;
	// Info for the rate limiter
	JLabel ratelabel;
	JSlider rateslider;
	// The transfer that we ask things to
	Transfer transfer;
	
	/**
	 * The constructor fo the transfer window.
	 * @param mf The frame it will be put on.
	 * @param isd True if this is supposed to be a download, else an upload.
	 * @param tr The transfer associated.
	 */
	public FiletransferWindow(MainFrame mf, boolean isd, Transfer tr) {
		transfer = tr;
		frame = mf;
		isdownload = isd;
		// Init the main window with a button
		main = new JPanel(new BorderLayout());
		// Add a button to it
		JButton close = new JButton("Close");
		close.addActionListener(new ButtonListener(frame, this));
		main.add(close, BorderLayout.SOUTH);
		
		// We init the window
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		// Add it
		main.add(panel, BorderLayout.CENTER);
		
		// Type + file
		panel.add(new JLabel(getTitle()));
		// Build from string
		String from = "";
		for (InetAddress a: transfer.getAddress()) {
			from += " " + a.getHostName() + ";";
		}
		// Find the user
		panel.add(new JLabel("From:" + from));
		// We have the general layout now
		// We say how much there is to do
		progresslabel = new JLabel();
		panel.add(progresslabel);
		// We add the progress bar
		progressbar = new JProgressBar();
		progressbar.setMinimum(0);
		progressbar.setMaximum(transfer.getLength());
		panel.add(progressbar);
		// The download rate
		rateslider = new JSlider(0, 100);
		rateslider.setSnapToTicks(true);
		// Listener
		rateslider.addChangeListener(new ChangeListener() {
			// If a change occurs, update ft and labels
			public void stateChanged(ChangeEvent arg0) {
				transfer.setRate(rateslider.getValue());
				updateRate();
			}
		});
		panel.add(rateslider);	
		// Rate label
		ratelabel = new JLabel();
		panel.add(ratelabel);
		updateRate();
		
		update();
	}
	
	/**
	 * Gets the transfer.
	 * @return The transfer.
	 */
	public Transfer getTransfer() {
		return transfer;
	}
	
	/**
	 * Update the info on the rate limiter.
	 */
	public void updateRate() {
		int rate = transfer.getRate();
		if (rate > 0 && rate < 1000) {
			ratelabel.setText("Speed: " + Integer.valueOf(rate)	.toString() +
					" max kbps.");
		} else {
			ratelabel.setText("Speed: max");
		}
		rateslider.setValue(rate);
	}
	
	/**
	 * Update The user interface.
	 */
	public void update() {
		String labeltext = "Transferred ";
		labeltext += Integer.valueOf(transfer.getLength() -
				transfer.getTodo()).toString();
		labeltext += " out of ";
		labeltext += Integer.valueOf(transfer.getLength());
		progresslabel.setText(labeltext);
		progressbar.setValue(transfer.getLength() - transfer.
				getTodo());
	}
	
	/**
	 * Get the title of the window, for the tabs.
	 * @return The title of the download window.
	 */
	public String getTitle() {
		String title;
		if (isdownload) {
			title = "Downloading: ";
		} else {
			title = "Uploading: ";
		}
		return title + transfer.getName();
	}
	
	/**
	 * Get the panel associated with this transfer.
	 * @return The panel to use.
	 */
	public JPanel getPanel() {
		return main;
	}
	
	
	/**
	 * Implements a listener for the close button.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class ButtonListener implements ActionListener {
		// The main frame
		MainFrame main;
		// The filetransfer window
		FiletransferWindow file;
		
		/**
		 * The constructor of a button listener.
		 * @param m The main frame.
		 * @param ft The file transfer.
		 */
		public ButtonListener(MainFrame m, FiletransferWindow ft) {
			main = m;
			file = ft;
		}
		
		/**
		 * Removes this from the search
		 */
		public void actionPerformed(ActionEvent arg0) {
			main.removeFiletransfer(file);
		}
	}
}
