package net.leiligheta.iscp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.util.Scanner;

import net.leiligheta.network.Server;

public class Receiver implements Runnable {
	private String address;
	private int port;
	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;
	private Server server;

	// Receiver values
	private boolean pwr;
	private int mvl;
	private int swl;
	private int ctl;
	private int sli;

	private static String inputSourceConvert(int number) {
		if (number < 10)
			return "0" + number;
		return Integer.toHexString(number);
	}

	public Receiver(String address, int port, Server server) {
		this.address = address;
		this.port = port;
		this.server = server;
		try {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			if (server != null) {
				new Thread(this).start();
			}
			sendCommand("PWRQSTN");
			sendCommand("MVLQSTN");
			sendCommand("SWLQSTN");
			sendCommand("SLIQSTN");
			sendCommand("CTLQSTN");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connected to receiver at: " + address + ":" + port);
	}

	private static String buildCommandString(String command) {
		String cmd = "ISCP";
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("10", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt(
				Integer.toHexString(command.length() + 2 + 1 + 16), 16);
		cmd += (char) Integer.parseInt("01", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += (char) Integer.parseInt("00", 16);
		cmd += "!1";
		cmd += command;
		cmd += (char) Integer.parseInt("0D", 16);
		return cmd.toUpperCase();
	}

	public void setPower(boolean power) {
		sendCommand("PWR" + (power ? "01" : "00"));
	}

	public void setVolume(int volume) {
		if (volume >= 0 && volume <= 100) {
			String hexVol = Integer.toHexString(volume);
			if (hexVol.length() < 2) {
				hexVol = "0" + hexVol;
			}
			sendCommand("MVL" + hexVol);
		}
	}

	public void setSubLevel(int level) {
		if (level >= -15 && level <= 12) {
			if (level == 0) {
				sendCommand("SWL00");
			} else if (level < 0) {
				sendCommand("SWL-" + Integer.toHexString(Math.abs(level)));
			} else if (level > 0) {
				sendCommand("SWL+" + Integer.toHexString(level));
			}
		}
	}

	public void setCenterLevel(int level) {
		if (level >= -12 && level <= 12) {
			if (level == 0) {
				sendCommand("CTL00");
			} else if (level < 0) {
				sendCommand("CTL-" + Integer.toHexString(Math.abs(level)));
			} else if (level > 0) {
				sendCommand("CTL+" + Integer.toHexString(level));
			}
		}
	}

	public void setInputSource(int sourceID) {
		sendCommand("SLI" + inputSourceConvert(sourceID));
	}

	public final void sendCommand(String command) {
		try {
			if (socket == null || !socket.isConnected()) {
				socket = new Socket(address, port);
			}
			out.writeBytes(buildCommandString(command));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getIn() {
		return in;
	}

//	public static void main(String[] args) {
//		final Receiver r = new Receiver("10.0.10.51", 60128, null);
//		Runnable client = new Runnable() {
//			@Override
//			public void run() {
//				String input;
//				while (r.getSocket().isConnected()) {
//					try {
//						input = r.getIn().readLine();
//						System.out.println(input.substring(
//								input.indexOf("!1") + 2, input.length() - 1));
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}
//				}
//			}
//		};
//		new Thread(client).start();
//		Scanner inputScanner = new Scanner(System.in);
//		while (true) {
//			r.sendCommand(inputScanner.nextLine());
//		}
//	}

	@Override
	public void run() {
		String input;
		while (socket.isConnected()) {
			try {
				input = in.readLine();
				input = input.substring(input.indexOf("!1") + 2,
						input.length() - 1);
				if (input.startsWith("PWR")) {
					pwr = input.substring(3).equals("01") ? true : false;
					server.updateReceiverPower(pwr);
				} else if (input.startsWith("MVL")) {
					mvl = Integer.parseInt(input.substring(3), 16);
					server.updateVolume(mvl, null);
				} else if (input.startsWith("SWL")) {
					if (input.charAt(3) == '0') {
						swl = 0;
					} else if (input.charAt(3) == '+') {
						swl = Integer.parseInt(input.substring(4), 16);
					} else if (input.charAt(3) == '-') {
						swl = Integer.parseInt(input.substring(4), 16);
					}
					server.updateSubLevel(swl, null);
				} else if (input.startsWith("CTL")) {
					if (input.charAt(3) == '0') {
						ctl = 0;
					} else if (input.charAt(3) == '+') {
						ctl = Integer.parseInt(input.substring(4), 16);
					} else if (input.charAt(3) == '-') {
						ctl = Integer.parseInt(input.substring(4), 16);
					}
					server.updateCenterLevel(ctl, null);
				} else if (input.startsWith("SLI")) {
					sli = Integer.parseInt(input.substring(3), 16);
					server.updateInputSource(sli);
				}
			} catch (IOException e) {
				break;
			}
		}
	}

	public boolean getPower() {
		return pwr;
	}

	public int getMainVolume() {
		return mvl;
	}

	public int getSubLevel() {
		return swl;
	}

	public int getCenterLevel() {
		return ctl;
	}

	public int getInputSource() {
		return sli;
	}

	public void setVolumeUp() {
		sendCommand("MVLUP");
	}

	public void setVolumeDown() {
		sendCommand("MVLDOWN");
	}
}
