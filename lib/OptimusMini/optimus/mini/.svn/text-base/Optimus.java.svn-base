package optimus.mini;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Java helper class to establish a connection to an Optimus Mini Three
 * keypad by Art Lebedev Studios. The RXTX implementation of the Java Comm API
 * is required in the classpath, and its JNI support library must be in
 * the java.library.path (JVM argument -Djava.library.path). Please see
 * <a href="http://code.google.com/p/optimus-mini-three/">the website</a>
 * for license terms and other information.<p/>
 * <p/>
 * This driver should still be considered BETA.
 *
 * @author Kai Erlemann <kai.erlemann@gmail.com>
 * @author Garrett Weinberg <garrett.weinberg@gmail.com>
 */
public class Optimus implements SerialPortEventListener {
	// -------------------------------------------------------------------------
	//
	// Attributes
	//
	// -------------------------------------------------------------------------
	private InputStream in;
	private OutputStream out;
	private int orientation = 0;
	private OptimusButtonListener buttonListener;

	// -------------------------------------------------------------------------
	//
	// Constant fields
	//
	// -------------------------------------------------------------------------
	public static final int DARK = 20;
	public static final int NORMAL = 40;
	public static final int BRIGHT = 60;

	// -------------------------------------------------------------------------
	//
	// Constructor
	//
	// -------------------------------------------------------------------------
	/**
	 * Creates a new Optimus Mini device connection.
	 *
	 * @param portName       the name of the comm port to connect to, e.g. 'COM9' or 'COM12'
	 * @param buttonListener the object that should receive button press notifications
	 */
	public Optimus(String portName, OptimusButtonListener buttonListener) {
		this.buttonListener = buttonListener;
		try {
			// open commport
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(portName);
			SerialPort serialPort = (SerialPort) portId.open("Optimus", 20000);

			// configure commport
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(1000000, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			serialPort.enableReceiveTimeout(400);

			// open data streams
			in = new BufferedInputStream(serialPort.getInputStream());
			out = new BufferedOutputStream(serialPort.getOutputStream(), 197);

			// add a shutdown hook that closes these streams on VM termination
			Runtime.getRuntime().addShutdownHook(new MyShutdownHook());

			// ignore all residual values on the comm port
			while (in.available() > 0) {
				in.read();
			}

		} catch (Exception e) {
			throw new Error("Error initializing Optimus Mini device", e);
		}
	}

	/**
	 * Turns the device on, but does <b>not</b> show any images on the buttons.
	 * Buttons must be set visible individually with the <code>showButton</code>
	 * method.
	 */
	public void turnOn() {
		sendData(2, 0, 0, new int[192]);
	}

	/**
	 * Switches the device off. The commport-connection to the device remains
	 * open, thus allowing the device to be switched on again. If the connection
	 * is to be permanently closed, use the <code>finalize</code>-method.
	 */
	public void turnOff() {
		sendData(3, 0, 0, new int[192]);
	}

	/**
	 * Switches on or refreshes the display of a button.
	 *
	 * @param button the number of the button between 1 and 3
	 */
	public void showButton(int button) {
		if (button >= 1 && button <= 3) {
			sendData(4, button, 0, new int[192]);
		} else
			throw new IllegalStateException("No button number " + button + " available");
	}

	/**
	 * Sets the brightness of the display for all three buttons.
	 *
	 * @param brightness one of the three values DARK, NORMAL or BRIGHT
	 */
	public void setBrightness(int brightness) {
		if (brightness == DARK || brightness == NORMAL || brightness == BRIGHT) {
			sendData(9, brightness, 0, new int[192]);
		} else
			throw new IllegalStateException("Illegal brightness value: " + brightness);
	}

	/**
	 * Sends a complete command line to the Optimus device and waits for a
	 * response. Used by all communication methods.
	 *
	 * @param command the number of the command (e.g. 1 to send an image)
	 * @param button  the number of the respective button (e.g. 1 for Button 1)
	 * @param pos     the position of the image data to be written (only used for
	 *                command 1, can be 0 in all other cases)
	 * @param data    an array of 192 data bytes to be used as image data
	 */
	private void sendData(int command, int button, int pos, int[] data) {
		try {
			int check = command + button + pos / 256 + pos % 256;
			out.write(command);
			out.write(button);
			out.write(pos / 256);
			out.write(pos % 256);
			for (int i = 0; i < 192; i++) {
				out.write(data[i]);
				check = (check + data[i]) % 256;
			}
			out.write(check);
			out.flush();

			in.read();
			in.read();
		} catch (Exception e) {
			throw new Error("Error writing data to the Optimus Mini device", e);
		}
	}

	/**
	 * Sends an image to the device and shows it on one of the buttons. The
	 * image is supposed to be 96x96 Pixels in size, larger images are being
	 * truncated. Keep in mind, that each image is rotated according to the
	 * current device orientation.
	 *
	 * @param image  the image to be shown
	 * @param button the number of the button between 1 and 3
	 */
	public void showImage(Image image, int button) {
		if (button < 1 || button > 3)
			throw new IllegalStateException("No button with the given id " + button);
		// rotate the image by the chosen orientation
		BufferedImage bi2 = new BufferedImage(96, 96,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi2.getGraphics();
		AffineTransform a = AffineTransform.getRotateInstance(Math.PI / 2
				* orientation, 48, 48);
		g2.setTransform(a);
		g2.drawImage(image, 0, 0, null);

		// write all image lines to the Optimus device
		for (int y = 0; y < 96; y++) {
			int[] line = convertImageLine(bi2, y);
			sendData(1, button, y * 192, line);
		}

		// refresh the button image
		showButton(button);
	}

	/**
	 * Convert a line of the given image to a data array that can be send to the
	 * device.
	 *
	 * @param bi the BufferedImage the data bytes are being created for
	 * @param y  the number of the line
	 * @return a data array in device dependent format
	 */
	private int[] convertImageLine(BufferedImage bi, int y) {
		int[] line = new int[192];
		int pos = 0;
		for (int x = 0; x < 96; x += 2) {
			int rgb1 = bi.getRGB(x + 1, y);
			int rgb2 = bi.getRGB(x, y);
			int r1 = rgb1 >> 0 & 255;
		int g1 = rgb1 >> 8 & 255;
			int b1 = rgb1 >> 16 & 255;
			int r2 = rgb2 >> 0 & 255;
			int g2 = rgb2 >> 8 & 255;
			int b2 = rgb2 >> 16 & 255;
			if (g1 < 8) {
				g1 = 0;
			}
			if (g2 < 8) {
				g2 = 0;
			}
			line[pos] = (b2 & 0xF8) + (g2 >> 5);
			line[pos + 1] = (r2 >> 3) + ((g2 & 0xFC) << 3);
			line[pos + 2] = (b1 & 0xF8) + (g1 >> 5);
			line[pos + 3] = (r1 >> 3) + ((g1 & 0xFC) << 3);
			pos += 4;
		}
		return line;
	}

	/**
	 * Set the orientation of the device. All images that are draw after this
	 * method is called will use the new orientation. Exisiting images will not
	 * automatically be updated but have to be re-send.
	 *
	 * @param orientation a value between 0 and 3, representing a rotation by 0, 90, 180
	 *                    or 270 degrees
	 */
	public void setOrientation(int orientation) {
		if (orientation >= 0 && orientation <= 3) {
			this.orientation = orientation;
		} else
			throw new IllegalStateException("Illegal orientation value: " + orientation);
	}

	/**
	 * Event handling method.
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		int type = event.getEventType();
		if (type == SerialPortEvent.DATA_AVAILABLE) {
			try {
				while (in.available() > 0) {
					int code = in.read();
					int whichButton = -1;
					// only interested in key press events
					if (code == 0x01) {
						switch (in.read()) {
						case 0x01:
							whichButton = 1;
							break;
						case 0x02:
							whichButton = 2;
							break;
						case 0x03:
							whichButton = 3;
							break;
						default:
							throw new IllegalStateException("unknown button number; is an Optimus Mini Three device attached?");
						}
						buttonListener.buttonPressed(whichButton);
					}
					// just drain any other data
					else {
						while (in.available() > 0) {
							in.read();
						}
					}
				}
			} catch (IOException e) {
				throw new Error("Error reading data from the Optimus Mini device", e);
			}
		}
	}

	private final class MyShutdownHook extends Thread {
		/**
		 * Close the connection to the device irrevocably.
		 */
		@Override
		public void run() {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				System.out.println("Error closing Optimus Mini serial connections");
				e.printStackTrace();
			} finally {
				in = null;
				out = null;
			}
		}
	}
}