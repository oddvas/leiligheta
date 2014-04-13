/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.leiligheta.gui.x10;

import javax.swing.JSlider;

import net.leiligheta.network.ClientMasterInterface;
import net.leiligheta.x10.X10Type;

/**
 * 
 * @author oddvar
 */
public class X10LampPanel extends X10Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider jSlider1;
	private int valuePressed;

	private void jSlider1MousePressed(java.awt.event.MouseEvent evt) {
		valuePressed = jSlider1.getValue();
	}

	private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {
		if (jSlider1.getValue() != valuePressed) {
			master.x10SetBrightness(houseCode + deviceCode,
					jSlider1.getValue(), null);
		}
	}

	public void setValue(int value) {
		jSlider1.setValue(value);
	}

	public X10LampPanel(String houseCode, int deviceCode, String description, boolean deviceOn,
			ClientMasterInterface master) {
		super(houseCode, deviceCode, description, X10Type.LAMP, deviceOn, master);
		jSlider1 = new JSlider();
		jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				jSlider1MousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				jSlider1MouseReleased(evt);
			}
		});
		jSlider1.setMaximum(22);
		jSlider1.setValue(11);
		jSlider1.setSnapToTicks(true);
		jPanel1.add(jSlider1, java.awt.BorderLayout.CENTER);
		jToggleButton1
		.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/img/lightbulb_off-50x50.png"))); // NOI18N

	}

}
