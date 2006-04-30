package org.lunarray.lshare.gui.incomplete;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

public class IncompleteList extends GUIFrame implements ActionListener,
        ListSelectionListener {

    private Timer timer;

    private IncompleteModel model;

    private int selrow;

    private JButton delete;

    public IncompleteList(LShare ls, MainGUI mg) {
        super(mg);

        // Setup table
        model = new IncompleteModel(ls);
        ls.getDownloadManager().addQueueListener(model);
        JTable t = new JTable(model);
        JScrollPane sp = new JScrollPane(t);
        t.setDefaultRenderer(JProgressBar.class, model);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getSelectionModel().addListSelectionListener(this);

        // Setup toolbar
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        
        delete = new JButton();
        delete.setActionCommand("delete");
        delete.addActionListener(this);
        delete.setEnabled(false);
        // delete.setText("Delete"); <- Use icon
        delete.setIcon(new ImageIcon("icons/edit-delete.png"));
        
        bar.add(delete);

        // Setup main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(sp, BorderLayout.CENTER);

        // Set frame
        frame.setTitle(getTitle());
        frame.setContentPane(mp);

        // Setup update timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateTable();
            }
        }, 0, 1000);
    }

    public void valueChanged(ListSelectionEvent arg0) {
        if (arg0.getFirstIndex() >= 0
                && arg0.getFirstIndex() < model.getRowCount()) {
            selrow = arg0.getFirstIndex();
        } else {
            selrow = -1;
        }
        setButtonsEnabled(selrow >= 0);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("delete")) {
            if (selrow >= 0) {
                model.getRow(selrow).delete();
                model.deleteRow(selrow);
            }
        }
    }

    @Override
    /**
     * Hides the frame.
     */
    public void close() {
        frame.setVisible(false);
        timer.cancel();
    }

    /**
     * Gets the title of the frame.
     * @return The title of the frame.
     */
    public String getTitle() {
        return "Incomplete";
    }

    private void setButtonsEnabled(boolean enabled) {
        delete.setEnabled(enabled);
    }
}
