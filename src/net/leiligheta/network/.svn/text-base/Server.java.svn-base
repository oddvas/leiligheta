package net.leiligheta.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static net.leiligheta.resources.CommandStrings.*;
import static net.leiligheta.resources.NetworkParameters.*;
import static net.leiligheta.resources.Services.*;

import net.leiligheta.db.Database;
import net.leiligheta.iscp.Receiver;
import net.leiligheta.x10.DeviceExistingException;
import net.leiligheta.x10.DeviceStateException;
import net.leiligheta.x10.X10;
import net.leiligheta.x10.X10Device;
import net.leiligheta.x10.X10Type;
//import net.leiligheta.xbmc.XBMCEventClient;
import net.leiligheta.xbmc.XBMCEventClient;

public class Server extends Thread implements ClientMasterInterface {
	
	// updateTimestamps
	private long lastVolumeUpdate;
	private long lastSubLevelUpdate;
	private long lastCenterLevelUpdate;

	private ServerSocket serverSocket;
	private UDPMulticastServer multicastServer;
	private Receiver receiver;
	private XBMCEventClient xbmcEventClient;
	private Database database;

	private List<Client> clients;

	private X10 x10;

	private synchronized void broadcast(String command) {
		multicastServer.send(command);
		ListIterator<Client> clientList = clients.listIterator();
		while (clientList.hasNext()) {
			Client c = clientList.next();
			try {
				c.send(command);
			} catch (IOException e) {
				clientList.remove();
			}
		}
	}

	private synchronized void broadcast(String command, Client client) {
		multicastServer.send(command);
		ListIterator<Client> clientList = clients.listIterator();
		while (clientList.hasNext()) {
			Client c = clientList.next();
			if (!c.equals(client)) {
				try {
					c.send(command);
				} catch (IOException e) {
					clientList.remove();
				}
			}
		}
	}

	public Server() {
		if (DATABASE_ENABLED) {
			database = new Database(DB_HOST, DB_PORT, DB_BASE_LEILIGHETA, DB_USERNAME, DB_PASSWORD);
		}
		clients = Collections.synchronizedList(new ArrayList<Client>());
		if (X10_ENABLED) {
			x10 = new X10("/dev/ttyS0");
			try {
				if (DATABASE_ENABLED) {
					x10.setDatabase(database);
					for (X10Device x10d : database.getX10Devices()) {
						x10.addX10Device(x10d);
					}
				} else {
					x10.addX10Device('L', 1, "Taklampe", X10Type.LAMP, false);
					// x10.addX10Device('L', 2, "Bordlampe", X10Type.LAMP, false);
					x10.addX10Device('L', 3, "Hjørne", X10Type.LAMP_NO_DIMMING, false);
					x10.addX10Device('L', 4, "Vindu", X10Type.LAMP, false);
					// x10.addX10Device('M', 1, "Kontrollpanel", X10Type.MONITOR, false);
					x10.addX10Device('M', 2, "Vegg", X10Type.MONITOR, false);
					x10.addX10Device('M', 3, "Kjøkken", X10Type.MONITOR, false);
					// x10.addX10Device('M', 4, "Kjøkken touch", X10Type.MONITOR, false);
					// x10.addX10Device('A', 1, "Forsterker", X10Type.AUDIO, false);
					x10.addX10Device('A', 2, "Sub", X10Type.AUDIO, false);
				}
			} catch (DeviceExistingException e) {

			}
		}
		if (MULTICAST_ENABLED) {
			multicastServer = new UDPMulticastServer(MULTICAST_ADDRESS,
					MULTICAST_PORT, this);
		}
		if (RECEIVER_ENABLED) {
			receiver = new Receiver(RECEIVER_ADDRESS, RECEIVER_PORT, this);
		}
		if (XBMC_EVENTS_ENABLED) {
			xbmcEventClient = new XBMCEventClient(XBMC_HOST, XBMC_PORT,
					this);
			xbmcEventClient.run();
		}
		lastVolumeUpdate = System.currentTimeMillis();
		start();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server running at: "
				+ serverSocket.getInetAddress().getHostAddress() + ":" + SERVER_PORT);
		while (true) {
			try {
				Client client = new Client(serverSocket.accept(), this);
				client.start();
				registerClient(client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized void registerClient(Client client) {
		clients.add(client);
		try {
			if (RECEIVER_ENABLED) {
				client.send(RECEIVER_CONTROL + ON);
				client.send(RECEIVER_POWER + (receiver.getPower() ? 1 : 0));
				client.send(RECEIVER_MAIN_VOLUME + receiver.getMainVolume());
				client.send(RECEIVER_SUB_LEVEL + receiver.getSubLevel());
				client.send(RECEIVER_SOURCE + receiver.getInputSource());
				client.send(RECEIVER_CENTER_LEVEL + receiver.getCenterLevel());
			} else {
				client.send(RECEIVER_CONTROL + OFF);
			}
			if (X10_ENABLED) {
				client.send(X10_CONTROL + ON);
				for (X10Device x10d : x10.getAllX10Devices()) {
					client.send(X10_ADD_DEVICE + x10d.getHouseCode() + ""
							+ x10d.getDeviceCode() + SPLIT_CHAR
							+ x10d.getDescription() + SPLIT_CHAR + x10d.getType() + SPLIT_CHAR + (x10d.isOn() ? ON : OFF));
					client.send(X10_DEVICE_STATE + x10d.getHouseCode() + ""
							+ x10d.getDeviceCode() + SPLIT_CHAR
							+ (x10d.isOn() ? ON : OFF) + SPLIT_CHAR + x10d.getValue());
				}
			} else {
				client.send(X10_CONTROL + OFF);
			}
		} catch (IOException e) {
			clients.remove(client);
		}
		System.out.println("Client connected: " + client.getHost());
	}

	public synchronized void unregisterClient(Client client) {
		clients.remove(client);
		System.out.println("Client disconnected: " + client.getHost());
	}

	@Override
	public synchronized void x10Power(String device, boolean on, Client client) {
		char houseCode = device.toUpperCase().charAt(0);
		int deviceCode = Integer.parseInt(device.substring(1));
		try {
			if (on) {
				x10.on(houseCode, deviceCode);
				broadcast(X10_ON + device);
			} else {
				x10.off(houseCode, deviceCode);
				broadcast(X10_OFF + device);
			}
		} catch (DeviceExistingException e) {
			if (!e.exists()) {
				try {
					client.send("Device " + e.getHouseCode() + ""
							+ e.getHouseCode() + "does not exist");
				} catch (IOException e1) {
					clients.remove(client);
				}
			}
		} catch (DeviceStateException e) {
			try {
				client.send(e.getHouseCode() + "" + e.getDeviceCode()
						+ " is already " + e.getDeviceState());
			} catch (IOException e1) {
				clients.remove(client);
			}
		}
	}

	@Override
	public synchronized void x10SetBrightness(String device, int units,
			Client client) {
		char houseCode = device.toUpperCase().charAt(0);
		int deviceCode = Integer.parseInt(device.substring(1));
		try {
			x10.setBrightness(houseCode, deviceCode, units);
			broadcast(X10_SET_BRIGHTNESS + device + SPLIT_CHAR + units);
		} catch (DeviceExistingException e) {
			if (!e.exists()) {
				try {
					client.send("Device " + e.getHouseCode() + ""
							+ e.getHouseCode() + "does not exist");
				} catch (IOException e1) {
					clients.remove(client);
				}
			}
		} catch (DeviceStateException e) {
			try {
				client.send(e.getHouseCode() + "" + e.getDeviceCode()
						+ " is already " + e.getDeviceState());
			} catch (IOException e1) {
				clients.remove(client);
			}
		}
	}

	@Override
	public synchronized void x10AllUnitsOff(String houseCode, Client client) {
		x10.allUnitsOff(houseCode.charAt(0));
		broadcast(X10_ALL_UNITS_OFF + houseCode);
	}

	@Override
	public synchronized void x10AllLightsOn(String houseCode, Client client) {
		x10.allLightsOn(houseCode.charAt(0));
		broadcast(X10_ALL_LIGHTS_ON + houseCode);
	}

	public static void main(String[] args) {
		new Server();
	}

	@Override
	public void receiverSetPower(String power, Client client) {
		receiver.setPower(power.equals("1") ? true : false);
	}

	@Override
	public void receiverSetMainVolume(int volume, Client client) {
		receiver.setVolume(volume);
		updateVolume(volume, client);
		lastVolumeUpdate = System.currentTimeMillis();
	}

	public void updateVolume(int volume, Client client) {
		if (client == null
				&& lastVolumeUpdate + 2000 > System.currentTimeMillis()) {
		} else {
			broadcast(RECEIVER_MAIN_VOLUME + volume, client);
		}
	}

	@Override
	public synchronized void x10AddDevice(String device, String description,
			String type, Client client, boolean deviceOn) {
		char houseCode = device.toUpperCase().charAt(0);
		int deviceCode = Integer.parseInt(device.substring(1));
		try {
			x10.addX10Device(houseCode, deviceCode, description,
					Enum.valueOf(X10Type.class, type.toUpperCase()), deviceOn);
			broadcast(X10_ADD_DEVICE + device + SPLIT_CHAR + description
					+ SPLIT_CHAR + type + SPLIT_CHAR + (deviceOn ? ON : OFF));
		} catch (DeviceExistingException e) {
			try {
				if (e.exists()) {
					client.send("Device " + e.getHouseCode() + ""
							+ e.getDeviceCode() + " already exists: "
							+ e.getDeviceDescription());
				}
			} catch (IOException e1) {
				clients.remove(client);
			}
		}
	}

	@Override
	public synchronized void x10RemoveDevice(String device, Client client) {
		char houseCode = device.toUpperCase().charAt(0);
		int deviceCode = Integer.parseInt(device.substring(1));
		try {
			x10.removeX10Device(houseCode, deviceCode);
			broadcast(X10_REMOVE_DEVICE + device);
		} catch (DeviceExistingException e) {
			try {
				client.send("Device " + e.getHouseCode() + ""
						+ e.getDeviceCode() + " does not exist");
			} catch (IOException e1) {
				clients.remove(client);
			}
		}
	}

	public void updateReceiverPower(boolean pwr) {
		broadcast(RECEIVER_POWER + (pwr ? 1 : 0));
	}

	@Override
	public void x10DeviceState(String device, String on, int units,
			Client client) {
		try {
			client.send(X10_DEVICE_STATE + device + SPLIT_CHAR + on
					+ SPLIT_CHAR + units);
		} catch (IOException ex) {
			clients.remove(client);
		}
	}

	public void xbmcPause() {
		for (X10Device x10d : x10.getLampX10Devices()) {
			if (x10d.isOn()) {
				try {
					x10.setBrightness(x10d.getHouseCode(),
							x10d.getDeviceCode(), x10d.getPreviousValue());
				} catch (DeviceExistingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DeviceStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void xbmcPlay() {
		for (X10Device x10d : x10.getLampX10Devices()) {
			if (x10d.isOn()) {
				try {
					x10.setBrightness(x10d.getHouseCode(),
							x10d.getDeviceCode(), 0);
				} catch (DeviceExistingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DeviceStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void updateSubLevel(int swl, Client client) {
		if (client == null
				&& lastSubLevelUpdate + 2000 > System.currentTimeMillis()) {
		} else {
			broadcast(RECEIVER_SUB_LEVEL + swl, client);
		}
	}

	public void updateCenterLevel(int ctl, Client client) {
		if (client == null
				&& lastCenterLevelUpdate + 2000 > System.currentTimeMillis()) {
		} else {
			broadcast(RECEIVER_CENTER_LEVEL + ctl, client);
		}
	}

	public void updateInputSource(int sourceID) {
		broadcast(RECEIVER_SOURCE + sourceID);
	}

	@Override
	public void receiverSetSubLevel(int level, Client client) {
		receiver.setSubLevel(level);
		updateSubLevel(level, client);
		lastSubLevelUpdate = System.currentTimeMillis();
	}

	@Override
	public void receiverSetMainVolumeUp(Client client) {
		receiver.setVolumeUp();
	}

	@Override
	public void receiverSetMainVolumeDown(Client client) {
		receiver.setVolumeDown();
	}

	@Override
	public void receiverSetInputSource(int sourceID, Client client) {
		receiver.setInputSource(sourceID);
	}

	@Override
	public void receiverSetCenterLevel(int level, Client client) {
		receiver.setCenterLevel(level);
		updateCenterLevel(level, client);
		lastCenterLevelUpdate = System.currentTimeMillis();
	}

	@Override
	public void x10Enabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiverEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}
	
	public Database getDatabase() {
		return database;
	}
}
