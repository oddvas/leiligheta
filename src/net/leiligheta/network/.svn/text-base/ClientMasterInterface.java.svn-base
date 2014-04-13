package net.leiligheta.network;

public interface ClientMasterInterface {
	// X10
	public void x10Enabled(boolean enabled);
	
	public void x10AllUnitsOff(String houseCode, Client client);

	public void x10AllLightsOn(String houseCode, Client client);

	public void x10Power(String device, boolean on, Client client);

	public void x10SetBrightness(String device, int units, Client client);

	public void x10DeviceState(String device, String on, int units,
			Client client);

	public void x10AddDevice(String device, String description, String type,
			Client client, boolean deviceOn);

	public void x10RemoveDevice(String device, Client client);

	// Receiver
	public void receiverEnabled(boolean enabled);
	
	public void receiverSetPower(String power, Client client);

	public void receiverSetMainVolume(int volume, Client client);

	public void receiverSetMainVolumeUp(Client client);

	public void receiverSetMainVolumeDown(Client client);

	public void receiverSetSubLevel(int level, Client client);

	public void receiverSetCenterLevel(int level, Client client);

	public void receiverSetInputSource(int sourceID, Client client);
}
