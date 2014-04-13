package optimus.mini;

public interface OptimusButtonListener {

	/**
	 * Invoked when button press events are received from the Optimus device.  There is
	 * unfortunately no event generated on button release -- only on button press.  This
	 * is a firmware limitation.
	 * @param buttonNumber 1 through 3
	 */
	public void buttonPressed(int buttonNumber);
}
