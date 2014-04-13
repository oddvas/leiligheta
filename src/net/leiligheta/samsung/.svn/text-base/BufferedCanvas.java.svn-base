package net.leiligheta.samsung;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BufferedCanvas {
	private int width;
	private int height;
	private ArrayList<Drawable> items;
	private BufferedImage image;
	private Graphics2D graphics;
	private Paint background;
	
	public BufferedCanvas(int width, int height, int type) {
		this.width = width;
		this.height = height;
		items = new ArrayList<Drawable>();
		image = new BufferedImage(width, height, type);
		graphics = (Graphics2D) image.getGraphics();
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		//graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		background = Color.BLACK;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void draw() {
		image.flush();
		graphics.setPaint(background);
		graphics.fillRect(0, 0, width, height);
		graphics.setPaint(Color.WHITE);
		for (Drawable d : items) {
			d.draw(graphics);
		}
	}
	
	public void add(Drawable item) {
		items.add(item);
	}
	
	public void setBackground(Paint background) {
		this.background = background;
	}
	
}
