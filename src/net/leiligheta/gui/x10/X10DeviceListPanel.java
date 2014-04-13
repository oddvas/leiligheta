/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * X10DeviceListPanel.java
 *
 * Created on 03.jan.2012, 20:25:01
 */

package net.leiligheta.gui.x10;

import java.awt.Point;

import net.leiligheta.network.ClientMasterInterface;
import net.leiligheta.x10.X10Type;

/**
 * 
 * @author oddvar
 */
public class X10DeviceListPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ClientMasterInterface master;

	/** Creates new form X10DeviceListPanel */
	public X10DeviceListPanel() {
		initComponents();
	}

	public void setMaster(ClientMasterInterface master) {
		this.master = master;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();

		setBorder(javax.swing.BorderFactory.createTitledBorder("X10"));

		jScrollPane1
		.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				jScrollPane1MousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				jScrollPane1MouseReleased(evt);
			}
		});
		jScrollPane1
		.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				jScrollPane1MouseDragged(evt);
			}
		});

		jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1,
				javax.swing.BoxLayout.PAGE_AXIS));
		jScrollPane1.setViewportView(jPanel1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
						jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306,
						javax.swing.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
						jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
						javax.swing.GroupLayout.PREFERRED_SIZE));
	}// </editor-fold>//GEN-END:initComponents

	private void jScrollPane1MouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jScrollPane1MouseDragged
		int position = releasedPointY + pressedPointY - evt.getPoint().y;
		if (position < 0) {
			position = 0;
			releasedPointY = 0;
		} else if (jScrollPane1.getComponents()[0].getHeight() - getHeight() > position) {
			position = jScrollPane1.getComponents()[0].getHeight()
					- getHeight();
			releasedPointY = jScrollPane1.getComponents()[0].getHeight()
					- getHeight();
		}
		jScrollPane1.getViewport().setViewPosition(new Point(0, position));
		jScrollPane1.repaint();
	}// GEN-LAST:event_jScrollPane1MouseDragged

	private int pressedPointY = 0;
	private int releasedPointY = 0;

	private void jScrollPane1MousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jScrollPane1MousePressed
		pressedPointY = evt.getPoint().y;
	}// GEN-LAST:event_jScrollPane1MousePressed

	private void jScrollPane1MouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jScrollPane1MouseReleased
		releasedPointY += pressedPointY - evt.getPoint().y;
	}// GEN-LAST:event_jScrollPane1MouseReleased

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;

	// End of variables declaration//GEN-END:variables

	public void addX10Device(String houseCode, int deviceCode,
			String description, X10Type type, boolean deviceOn) {
		if (type == X10Type.LAMP) {
			jPanel1.add(new X10LampPanel(houseCode, deviceCode, description, deviceOn,
					master));
		} else {
			jPanel1.add(new X10AppliancePanel(houseCode, deviceCode,
					description, type, deviceOn, master));
		}
	}

	public void removeX10Device(String houseCode, int deviceCode) {
		int removeIndex = -1;
		for (int i = 0; i < jPanel1.getComponentCount(); i++) {
			if (jPanel1.getComponent(i) instanceof X10Panel) {
				X10Panel x10p = (X10Panel) jPanel1.getComponent(i);
				if (x10p.getHouseCode().equals(houseCode)) {
					if (x10p.getDeviceCode() == deviceCode) {
						removeIndex = i;
						break;
					}
				}
			}
		}
		if (removeIndex >= 0) {
			jPanel1.remove(removeIndex);
		}
	}

	public X10Panel getX10Device(String houseCode, int deviceCode) {
		for (int i = 0; i < jPanel1.getComponentCount(); i++) {
			if (jPanel1.getComponent(i) instanceof X10Panel) {
				X10Panel x10p = (X10Panel) jPanel1.getComponent(i);
				if (x10p.getHouseCode().equals(houseCode)) {
					if (x10p.getDeviceCode() == deviceCode)
						return x10p;
				}
			}
		}
		return null;
	}

	public X10Panel getX10Device(String device) {
		return getX10Device(device.substring(0, 1),
				Integer.parseInt(device.substring(1)));
	}
}