package net.leiligheta.network;

import java.io.IOException;
import java.net.MulticastSocket;

import static net.leiligheta.resources.NetworkParameters.*;

public class UDPMulticastClient {
	private MulticastSocket socket;
	private ClientMasterInterface master;
	
	public UDPMulticastClient(String address, ClientMasterInterface master) {
		this.master = master;
		try {
			socket = new MulticastSocket(MULTICAST_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
