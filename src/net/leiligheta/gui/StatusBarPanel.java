/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StatusBarPanel.java
 *
 * Created on 17.jan.2012, 02:05:03
 */

package net.leiligheta.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author oddvar
 */
public class StatusBarPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Creates new form StatusBarPanel */
	public StatusBarPanel() {
		initComponents();
		Timer t = new Timer("UpdateTime", true);
		t.scheduleAtFixedRate(new TimerTask() {
			private String getDate() {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date date = new Date();
				return dateFormat.format(date);
			}

			private String getTime() {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				return dateFormat.format(date);
			}

			@Override
			public void run() {
				dateLabel.setText(getDate());
				timeLabel.setText(getTime());
			}

		}, 0, 1000);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		dateLabel = new javax.swing.JLabel();
		timeLabel = new javax.swing.JLabel();

		setBackground(new java.awt.Color(0, 0, 0));
		setForeground(new java.awt.Color(255, 255, 255));
		setMinimumSize(new java.awt.Dimension(132, 22));
		setLayout(new java.awt.GridBagLayout());

		dateLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		dateLabel.setForeground(new java.awt.Color(255, 255, 255));
		dateLabel.setText("date");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 0.8;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(dateLabel, gridBagConstraints);

		timeLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		timeLabel.setForeground(new java.awt.Color(255, 255, 255));
		timeLabel.setText("time");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(timeLabel, gridBagConstraints);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel dateLabel;
	private javax.swing.JLabel timeLabel;
	// End of variables declaration//GEN-END:variables

}