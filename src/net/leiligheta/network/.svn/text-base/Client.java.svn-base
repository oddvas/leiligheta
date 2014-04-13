package net.leiligheta.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static net.leiligheta.resources.CommandStrings.*;

public class Client extends Thread {
	private Socket socket;
	private ClientMasterInterface master;
	private BufferedReader in;
	private BufferedWriter out;

	public Client(Socket socket, ClientMasterInterface master) {
		this.socket = socket;
		this.master = master;
	}

	public Client(String host, int port, ClientMasterInterface master)
			throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		this.master = master;
	}
	
	public String getHost() {
		return socket.getInetAddress().getHostAddress();
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (socket.isConnected()) {
			try {
								String inn = in.readLine();
								System.out.println(inn);
								String[] input = inn.split(SPLIT_CHAR);
				//String[] input = in.readLine().split(SPLIT_CHAR);
				if (input[0].equalsIgnoreCase(HELP)) {
					send("leiligheta.net - help\nx10: x10 \nReceiver: r\n");
				}
				if (input[0].equalsIgnoreCase(X10)) {
					if (input[1].equalsIgnoreCase(CONTROL)) {
						master.x10Enabled(input[2].equals(ON) ? true : false);
					} else if (input[1].equalsIgnoreCase(_X10_ALL_LIGHTS_ON)) {
						master.x10AllLightsOn(input[2], this);
					} else if (input[1].equalsIgnoreCase(_X10_ALL_UNITS_OFF)) {
						master.x10AllUnitsOff(input[2], this);
					} else if (input[1].equalsIgnoreCase(ON)) {
						master.x10Power(input[2], true, this);
					} else if (input[1].equalsIgnoreCase(OFF)) {
						master.x10Power(input[2], false, this);
					} else if (input[1].equalsIgnoreCase(_X10_SET_BRIGHTNESS)) {
						master.x10SetBrightness(input[2],
								Integer.parseInt(input[3]), this);
					} else if (input[1].equalsIgnoreCase(_X10_DEVICE_STATE)) {
						master.x10DeviceState(input[2], input[3],
								Integer.parseInt(input[4]), this);
					} else if (input[1].equalsIgnoreCase(_X10_ADD_DEVICE)) {
						master.x10AddDevice(input[2], input[3], input[4], this, input[5].equals(ON) ? true : false);
					} else if (input[1].equalsIgnoreCase(_X10_REMOVE_DEVICE)) {
						master.x10RemoveDevice(input[2], this);
					}
				} else if (input[0].equalsIgnoreCase(RECEIVER)) {
					if (input[1].equalsIgnoreCase(CONTROL)) {
						master.receiverEnabled(input[2].equals(ON) ? true : false);
					} else if (input[1].equalsIgnoreCase(_RECEIVER_POWER)) {
						master.receiverSetPower(input[2], this);
					} else if (input[1].equalsIgnoreCase(_RECEIVER_MAIN_VOLUME)) {
						if (input[2].equalsIgnoreCase(UP)) {
							master.receiverSetMainVolumeUp(this);
						} else if (input[2].equalsIgnoreCase(DOWN)) {
							master.receiverSetMainVolumeDown(this);
						} else {
							master.receiverSetMainVolume(
									Integer.parseInt(input[2]), this);
						}
					} else if (input[1].equalsIgnoreCase(_RECEIVER_SUB_LEVEL)) {
						master.receiverSetSubLevel(Integer.parseInt(input[2]),
								this);
					} else if (input[1].equalsIgnoreCase(_RECEIVER_CENTER_LEVEL)) {
						master.receiverSetCenterLevel(Integer.parseInt(input[2]),
								this);
					} else if (input[1].equalsIgnoreCase(_RECEIVER_SOURCE)) {
						master.receiverSetInputSource(Integer.parseInt(input[2]), this);
					}

				} else if (input[0].equalsIgnoreCase(EXIT)
						|| input[0].equalsIgnoreCase(DISCONNECT)) {
					socket.close();
					if (master instanceof Server) {
						((Server) master).unregisterClient(this);
					}
					break;
				}
			} catch (Exception e) {
				if (master instanceof Server) {
					((Server) master).unregisterClient(this);
				}
				break;
			}
		}
	}

	public void send(String command) throws IOException {
		if (out == null) {
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream(), "UTF-8"));
		}
		out.write(command + NEWLINE_CHAR);
		out.flush();
	}

	public void setVolume(int volume) throws IOException {
		send(RECEIVER_MAIN_VOLUME + volume);
	}

	public void setSubLevel(int level) throws IOException {
		send(RECEIVER_SUB_LEVEL + level);
	}

	public void setCenterLevel(int level) throws IOException {
		send(RECEIVER_CENTER_LEVEL + level);
	}

	public void setInputSource(int sourceID) throws IOException {
		send(RECEIVER_SOURCE + sourceID);
	}

}
