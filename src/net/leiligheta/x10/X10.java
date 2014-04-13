package net.leiligheta.x10;

import java.io.IOException;
import java.util.ArrayList;

import net.leiligheta.db.Database;

import com.jpeterson.x10.Gateway;
import com.jpeterson.x10.SerialGateway;
import com.jpeterson.x10.Transmitter;
import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.AllLightsOnEvent;
import com.jpeterson.x10.event.AllUnitsOffEvent;
import com.jpeterson.x10.event.BrightEvent;
import com.jpeterson.x10.event.DimEvent;
import com.jpeterson.x10.event.OffEvent;
import com.jpeterson.x10.event.OnEvent;

public class X10 {
	private Transmitter transmitter;
	private static String transmitterName = "com.jpeterson.x10.module.CM11A";
	public static final int LIGHT_MAX_VALUE = 22;
	private ArrayList<X10Device> x10Devices = new ArrayList<X10Device>();
	
	private Database database;

	public X10(String port) {
		try {
			transmitter = (Transmitter) Class.forName(transmitterName)
					.newInstance();
		} catch (ClassNotFoundException e) {
			System.err.println("Unknown class: " + transmitterName);
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("Error instantiating " + transmitterName);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Illegally accessing " + transmitterName);
			e.printStackTrace();
		}

		if (transmitter instanceof Gateway) {
			Gateway gateway = (Gateway) transmitter;
			if (transmitter instanceof SerialGateway && port != null) {
				((SerialGateway) transmitter).setPortName(port);
			}
			try {
				gateway.allocate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void on(char houseCode, int deviceCode) throws DeviceStateException,
	DeviceExistingException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d != null) {
			if (!x10d.isOn()) {
				try {
					if (x10d.getType() == X10Type.LAMP) {
						transmitter.transmit(new AddressEvent(this, houseCode,
								deviceCode));
						transmitter.transmit(new DimEvent(this, houseCode,
								LIGHT_MAX_VALUE - x10d.getValue(),
								LIGHT_MAX_VALUE));
					} else {
						transmitter.transmit(new AddressEvent(this, houseCode,
								deviceCode));
						transmitter.transmit(new OnEvent(this, houseCode));
					}
					x10d.setDeviceOn(true);
					if (database != null) {
						database.updateX10Device(x10d);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				throw new DeviceStateException(houseCode, deviceCode, "ON");
		} else
			throw new DeviceExistingException(houseCode, deviceCode, false);
	}

	public void off(char houseCode, int deviceCode)
			throws DeviceStateException, DeviceExistingException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d != null) {
			if (x10d.isOn()) {
				try {
					transmitter.transmit(new AddressEvent(this, houseCode,
							deviceCode));
					transmitter.transmit(new OffEvent(this, houseCode));
					x10d.setDeviceOn(false);
					if (database != null) {
						database.updateX10Device(x10d);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				throw new DeviceStateException(houseCode, deviceCode, "OFF");
		} else
			throw new DeviceExistingException(houseCode, deviceCode, false);
	}

	public void setBrightness(char houseCode, int deviceCode, int units)
			throws DeviceExistingException, DeviceStateException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d != null) {
			if (x10d.getType() == X10Type.LAMP) {
				if (units <= LIGHT_MAX_VALUE && units >= 0) {
					try {
						if (!x10d.isOn()) {
							x10d.setValue(LIGHT_MAX_VALUE);
						}
						if (units > x10d.getValue()) {
							transmitter.transmit(new AddressEvent(this,
									houseCode, deviceCode));
							transmitter.transmit(new BrightEvent(this,
									houseCode, units - x10d.getValue(),
									LIGHT_MAX_VALUE));
						} else if (units < x10d.getValue()) {
							transmitter.transmit(new AddressEvent(this,
									houseCode, deviceCode));
							transmitter.transmit(new DimEvent(this, houseCode,
									x10d.getValue() - units, LIGHT_MAX_VALUE));
						} else if (!x10d.isOn()) {
							transmitter.transmit(new AddressEvent(this,
									houseCode, deviceCode));
							transmitter.transmit(new OnEvent(this, houseCode));
						}
						x10d.setDeviceOn(true);
						x10d.setValue(units);
						if (database != null) {
							database.updateX10Device(x10d);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					throw new DeviceStateException(houseCode, deviceCode,
							"VALUE OUT OF RANGE. MUST BE BETWEEN 0 AND 22");
			} else
				throw new DeviceStateException(houseCode, deviceCode,
						"NOT LAMP");
		} else
			throw new DeviceExistingException(houseCode, deviceCode, false);
	}

	public void allLightsOn(char houseCode) {
		try {
			transmitter.transmit(new AllLightsOnEvent(this, houseCode));
			for (X10Device x10d : x10Devices) {
				if (x10d.getHouseCode() == houseCode) {
					if (x10d.getType() == X10Type.LAMP) {
						x10d.setDeviceOn(true);
						x10d.setValue(22);
						if (database != null) {
							database.updateX10Device(x10d);
						}
					} else if (x10d.getType() == X10Type.LAMP_NO_DIMMING) {
						x10d.setDeviceOn(true);
						if (database != null) {
							database.updateX10Device(x10d);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void allUnitsOff(char houseCode) {
		try {
			transmitter.transmit(new AllUnitsOffEvent(this, houseCode));
			for (X10Device x10d : x10Devices) {
				if (x10d.getHouseCode() == houseCode) {
					x10d.setDeviceOn(false);
					if (database != null) {
						database.updateX10Device(x10d);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public X10Device getDevice(char houseCode, int deviceCode) {
		for (X10Device x10d : x10Devices) {
			if (x10d.getHouseCode() == houseCode) {
				if (x10d.getDeviceCode() == deviceCode)
					return x10d;
			}
		}
		return null;
	}

	public X10Device findDeviceByDescription(String description) {
		for (X10Device x10d : x10Devices) {
			if (x10d.getDescription().equals(description))
				return x10d;
		}
		return null;
	}

	public ArrayList<X10Device> getAllX10Devices() {
		return x10Devices;
	}

	public ArrayList<X10Device> getApplianceX10Devices() {
		ArrayList<X10Device> appliances = new ArrayList<X10Device>();
		for (X10Device x10d : x10Devices) {
			if (x10d.getType() == X10Type.APPLIANCE) {
				appliances.add(x10d);
			}
		}
		return appliances;
	}

	public ArrayList<X10Device> getLampX10Devices() {
		ArrayList<X10Device> lamps = new ArrayList<X10Device>();
		for (X10Device x10d : x10Devices) {
			if (x10d.getType() == X10Type.LAMP
					|| x10d.getType() == X10Type.LAMP_NO_DIMMING) {
				lamps.add(x10d);
			}
		}
		return lamps;
	}

	public ArrayList<X10Device> getAudioX10Devices() {
		ArrayList<X10Device> audio = new ArrayList<X10Device>();
		for (X10Device x10d : x10Devices) {
			if (x10d.getType() == X10Type.AUDIO) {
				audio.add(x10d);
			}
		}
		return audio;
	}

	public ArrayList<X10Device> getMonitorX10Devices() {
		ArrayList<X10Device> monitors = new ArrayList<X10Device>();
		for (X10Device x10d : x10Devices) {
			if (x10d.getType() == X10Type.MONITOR) {
				monitors.add(x10d);
			}
		}
		return monitors;
	}
	
	public void addX10Device(X10Device x10d) throws DeviceExistingException {
		X10Device x10dE = getDevice(x10d.getHouseCode(), x10d.getDeviceCode());
		if (x10dE == null) {
			x10Devices.add(x10d);
		} else
			throw new DeviceExistingException(x10dE.getHouseCode(), x10dE.getDeviceCode(),
					x10dE.getDescription(), true);
	}	

	public void addX10Device(char houseCode, int deviceCode,
			String description, X10Type type, boolean deviceOn) throws DeviceExistingException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d == null) {
			x10Devices.add(new X10Device(deviceCode, houseCode, description,
					type, deviceOn));
		} else
			throw new DeviceExistingException(houseCode, deviceCode,
					x10d.getDescription(), true);
	}

	public void addX10Device(char houseCode, int deviceCode,
			String description, X10Type type, boolean deviceOn, int value)
					throws DeviceExistingException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d == null) {
			x10Devices.add(new X10Device(deviceCode, houseCode, description,
					type, deviceOn, value));
		} else
			throw new DeviceExistingException(houseCode, deviceCode,
					x10d.getDescription(), true);
	}

	public void removeX10Device(char houseCode, int deviceCode)
			throws DeviceExistingException {
		X10Device x10d = getDevice(houseCode, deviceCode);
		if (x10d != null) {
			x10Devices.remove(x10d);
		} else
			throw new DeviceExistingException(houseCode, deviceCode, false);
	}

	public void setDatabase(Database database) {
		this.database = database;		
	}
}
