/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.leiligheta.gui.x10;

import net.leiligheta.network.ClientMasterInterface;
import net.leiligheta.x10.X10Type;

/**
 * 
 * @author oddvar
 */
public class X10AppliancePanel extends X10Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public X10AppliancePanel(String houseCode, int deviceCode,
			String description, X10Type type, boolean deviceOn, ClientMasterInterface master) {
		super(houseCode, deviceCode, description, type, deviceOn, master);
		if (type == X10Type.LAMP_NO_DIMMING) {
			jToggleButton1
			.setIcon(new javax.swing.ImageIcon(
					getClass()
					.getResource(
							(deviceOn) ? "/res/img/lightbulb_on-50x50.png" : "/res/img/lightbulb_off-50x50.png"))); // NOI18N
		}
	}

}
