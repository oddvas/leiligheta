package net.leiligheta.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticastServer {

	public static final int MULTICAST_BUFFER_LENGTH = 1024;

	private MulticastSocket multicastSocket;
	private int multicastPort;
	private byte[] multicastBuffer;
	private InetAddress target;

	public UDPMulticastServer(String multicastAddress, int multicastPort,
			Server master) {
		this.multicastPort = multicastPort;
		multicastBuffer = new byte[MULTICAST_BUFFER_LENGTH];
		try {
			multicastSocket = new MulticastSocket(multicastPort);
			target = InetAddress.getByName(multicastAddress);
			multicastSocket.joinGroup(target);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("UDP Multicast server running at: "
				+ multicastAddress + ":" + multicastPort);
	}

	public void send(String message) {
		multicastBuffer = message.getBytes();
		try {
			multicastSocket.send(new DatagramPacket(multicastBuffer,
					multicastBuffer.length, target, multicastPort));
			System.out.println("sending udp: " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
