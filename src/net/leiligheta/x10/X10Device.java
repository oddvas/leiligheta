package net.leiligheta.x10;

import java.io.Serializable;

public class X10Device implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -667466030948752139L;
	private int deviceCode;
	private char houseCode;
	private String description;
	private X10Type type;
	private int value;
	private boolean deviceOn = false;
	private int previousValue;

	public X10Device(int deviceCode, char houseCode, String description,
			X10Type type, boolean deviceOn) {
		this.deviceCode = deviceCode;
		this.houseCode = houseCode;
		this.description = description;
		this.type = type;
		if (type == X10Type.LAMP) {
			value = 0;
		}
		this.deviceOn = deviceOn;
		System.out.println("added device: " + deviceCode + houseCode
				+ description);
	}

	public X10Device(int deviceCode, char houseCode, String description,
			X10Type type, boolean deviceOn, int value) {
		this.deviceCode = deviceCode;
		this.houseCode = houseCode;
		this.description = description;
		this.type = type;
		this.value = value;
		this.deviceOn = deviceOn;
		System.out.println("added device: " + deviceCode + houseCode
				+ description + value + deviceOn);
	}

	public int getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}

	public char getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(char houseCode) {
		this.houseCode = houseCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public X10Type getType() {
		return type;
	}

	public void setType(X10Type type) {
		this.type = type;
	}

	public void setValue(int value) {
		previousValue = this.value;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setDeviceOn(boolean deviceOn) {
		this.deviceOn = deviceOn;
	}

	public boolean isOn() {
		return deviceOn;
	}

	public void setPreviousValue(int previousValue) {
		this.previousValue = previousValue;
	}

	public int getPreviousValue() {
		return previousValue;
	}

}
