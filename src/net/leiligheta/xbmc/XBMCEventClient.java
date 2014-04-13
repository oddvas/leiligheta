package net.leiligheta.xbmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.leiligheta.network.Server;

public class XBMCEventClient implements Runnable {
	private Socket socket;
	private BufferedReader in;
	public XBMCEventClient(String address, int port, Server server) {
		try {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			// in = new InputStreamReader(socket.getInputStream());
			if (server != null) {
				new Thread(this).start();
			}
			System.out.println("Connected to XBMC at: " + address + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (socket.isConnected()) {
			try {
				// char inp = Character.in.read();
				String input = in.readLine();
				System.out.println(input);
				System.out.println(input.contains("Player.OnPause"));
				if (input.contains("Player.OnPause")
						&& !input.contains("\"type\":\"song\"")) {
					// server.xbmcPause();
					System.out.println("onpause");
				} else if (input.contains("Player.OnPlay")
						&& !input.contains("\"type\":\"song\"")) {
					// server.xbmcPlay();
					System.out.println("onplay");
				}
			} catch (IOException e) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new XBMCEventClient("xbmc.leiligheta.net", 9090, null))
		.start();
	}

}
