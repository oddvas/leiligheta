package optimus.mini;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OptimusTester extends JFrame implements ActionListener, Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optimus o;
	private static final String commPort = "/dev/ttyUSB0";

	private JButton on = new JButton("on");
	private JButton off = new JButton("off");

	private JButton dark = new JButton("dark");
	private JButton normal = new JButton("normal");
	private JButton bright   = new JButton("bright");

	private JButton button1 = new JButton("Button 1");
	private JButton button2 = new JButton("Button 2");
	private JButton button3 = new JButton("Button 3");

	public OptimusTester()
	{
		super("Optimus Mini");

		JPanel p1 = new JPanel( new GridLayout(0,1));
		JPanel p2 = new JPanel( new GridLayout(0,1));
		JPanel p3 = new JPanel( new GridLayout(0,1));

		p1.add(on);
		p1.add(off);

		p2.add(dark);
		p2.add(normal);
		p2.add(bright);

		p3.add(button1);
		p3.add(button2);
		p3.add(button3);

		JButton[] buttons = new JButton[] {on,off,dark,normal,bright,button1,button2,button3};
		for (JButton button : buttons) {
			button.addActionListener(this);
		}


		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(p1);
		getContentPane().add(p2);
		getContentPane().add(p3);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		o = new Optimus(commPort, new OptimusButtonListener() {
			@Override
			public void buttonPressed(int buttonNumber) {
				System.out.println("button " + buttonNumber + " pressed.");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent ae )
	{
		Object s = ae.getSource();
		if( s==on ) {
			o.turnOn();
		}
		if( s==off ) {
			o.turnOff();
		}
		if( s==dark ) {
			o.setBrightness(20);
		}
		if( s==normal ) {
			o.setBrightness(40);
		}
		if( s==bright ) {
			o.setBrightness(60);
		}
		if( s==button1 ) {
			o.showButton(1);
		}
		if( s==button2 ) {
			o.showButton(2);
		}
		if( s==button3 )
		{
			BufferedImage bi = new BufferedImage(96,96,BufferedImage.TYPE_INT_RGB);
			bi.getGraphics().setColor(new Color(255,255,0));
			bi.getGraphics().fillOval(0,0,95,95);
			o.showImage(bi,3);
			new Thread(this).start();
		}
	}

	@Override
	public void run()
	{
		int pos = 0;
		while( true )
		{
			for( int i=0; i<3; i++ )
			{
				BufferedImage bi = new BufferedImage(96,96,BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.getGraphics();
				g.setColor(Color.getHSBColor(.333f*i,1,.5f));
				g.fillRect(0,0,96,96);
				g.setColor(Color.black);
				g.setFont(new Font("Arial",Font.PLAIN,50));
				g.drawString("Optimus Mini - The next generation keyboard!",pos-i*96,80);
				o.showImage(bi,3-i);
			}
			pos-=20;
		}
	}


	public static void main( String[] args )
	{
		new OptimusTester();
	}
}