package net.leiligheta.x10;

public class DeviceExistingException extends Exception {
	private char houseCode;
	private int deviceCode;
	private boolean exists;
	private String description;

	public DeviceExistingException(char houseCode, int deviceCode,
			boolean exists) {
		this.houseCode = houseCode;
		this.deviceCode = deviceCode;
		this.exists = exists;
	}

	public DeviceExistingException(char houseCode, int deviceCode,
			String description, boolean exists) {
		this.houseCode = houseCode;
		this.deviceCode = deviceCode;
		this.description = description;
		this.exists = exists;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7122943765447827238L;

	public char getHouseCode() {
		return houseCode;
	}

	public int getDeviceCode() {
		return deviceCode;
	}

	public boolean exists() {
		return exists;
	}

	public String getDeviceDescription() {
		return description;
	}

}
