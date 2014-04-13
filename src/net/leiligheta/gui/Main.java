/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Main.java
 *
 * Created on 03.jan.2012, 18:50:02
 */

package net.leiligheta.gui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.leiligheta.gui.receiver.ReceiverPanel;
import net.leiligheta.gui.x10.X10LampPanel;
import net.leiligheta.gui.x10.X10Panel;
import net.leiligheta.network.Client;
import net.leiligheta.network.ClientMasterInterface;
import net.leiligheta.x10.X10Type;

import static net.leiligheta.resources.CommandStrings.*;
import static net.leiligheta.resources.NetworkParameters.*;

/**
 * 
 * @author oddvar
 */
public class Main extends javax.swing.JFrame implements ClientMasterInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Client client;

	/** Creates new form Main */
	public Main() {
		// setUndecorated(true);
		initComponents();
		receiverPanel1.setMaster(this);
		x10DeviceListPanel1.setMaster(this);
		try {
			client = new Client(REMOTE_HOST, SERVER_PORT, this);
			new Thread(client).start();
		} catch (UnknownHostException ex) {
			Logger.getLogger(ReceiverPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ReceiverPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		// setBounds(0, 0, 800, 600);
		// setAlwaysOnTop(true);
		// BufferedImage cursorImg = new BufferedImage(16, 16,
		// BufferedImage.TYPE_INT_ARGB);
		//
		// // Create a new blank cursor.
		// Cursor blankCursor =
		// Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new
		// Point(0, 0), "blank cursor");
		//
		// setCursor(blankCursor);
		//
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds(0,0,screenSize.width, screenSize.height);
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

		statusBarPanel1 = new net.leiligheta.gui.StatusBarPanel();
		jPanel1 = new javax.swing.JPanel();
		receiverPanel1 = new net.leiligheta.gui.receiver.ReceiverPanel();
		x10DeviceListPanel1 = new net.leiligheta.gui.x10.X10DeviceListPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Leiligheta");
		getContentPane().add(statusBarPanel1, java.awt.BorderLayout.PAGE_END);

		jPanel1.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(receiverPanel1, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(x10DeviceListPanel1, gridBagConstraints);

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private net.leiligheta.gui.receiver.ReceiverPanel receiverPanel1;
	private net.leiligheta.gui.StatusBarPanel statusBarPanel1;
	private net.leiligheta.gui.x10.X10DeviceListPanel x10DeviceListPanel1;

	// End of variables declaration//GEN-END:variables

	@Override
	public void x10AllUnitsOff(String houseCode, Client client) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void x10AllLightsOn(String houseCode, Client client) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void x10Power(String device, boolean on, Client client) {
		if (client == null) {
			try {
				if (on) {
					this.client.send(X10_ON + device);
				} else {
					this.client.send(X10_OFF + device);
				}
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			x10DeviceListPanel1.getX10Device(device).setOn(on);
		}
	}

	@Override
	public void x10SetBrightness(String device, int units, Client client) {
		if (client == null) {
			try {
				this.client.send(X10_SET_BRIGHTNESS + device + SPLIT_CHAR
						+ units);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			X10LampPanel x10d = (X10LampPanel) x10DeviceListPanel1
					.getX10Device(device);
			x10d.setOn(true);
			x10d.setValue(units);
		}
	}

	public void x10AddDevice(String device, String description, String type,
			Client client, boolean deviceOn) {
		x10DeviceListPanel1.addX10Device(device.substring(0, 1),
				Integer.parseInt(device.substring(1)), description,
				X10Type.valueOf(type.toUpperCase()), deviceOn);
	}

	public void x10RemoveDevice(String device, Client client) {
		x10DeviceListPanel1.removeX10Device(device.substring(0, 1),
				Integer.parseInt(device.substring(1)));
	}

	@Override
	public void receiverSetPower(String power, Client client) {
		if (client == null) {
			try {
				this.client.send(RECEIVER_POWER + power);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			receiverPanel1.setPower(power.equals("1") ? true : false);
		}
	}

	@Override
	public void receiverSetMainVolume(int volume, Client client) {
		if (client == null) {
			try {
				this.client.setVolume(volume);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			receiverPanel1.setMainVolume(volume);
		}
	}

	@Override
	public void x10DeviceState(String device, String on, int units,
			Client client) {
		X10Panel x10d = x10DeviceListPanel1.getX10Device(device);
		if (x10d != null) {
			x10d.setOn(on.equals("1") ? true : false);
		}
		if (x10d instanceof X10LampPanel) {
			((X10LampPanel) x10d).setValue(units);
			System.out.println(units);
		}
	}

	@Override
	public void receiverSetSubLevel(int level, Client client) {
		if (client == null) {
			try {
				this.client.setSubLevel(level);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			receiverPanel1.setSubLevel(level);
		}
	}

	@Override
	public void receiverSetMainVolumeUp(Client client) {

	}

	@Override
	public void receiverSetMainVolumeDown(Client client) {

	}

	@Override
	public void receiverSetInputSource(int sourceID, Client client) {
		if (client == null) {
			try {
				this.client.setInputSource(sourceID);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			receiverPanel1.setInputSource(sourceID);
		}
	}

	@Override
	public void receiverSetCenterLevel(int level, Client client) {
		if (client == null) {
			try {
				this.client.setCenterLevel(level);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			receiverPanel1.setCenterLevel(level);
		}
	}

	@Override
	public void x10Enabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiverEnabled(boolean enabled) {
		receiverPanel1.setControlEnabled(enabled);
	}

}