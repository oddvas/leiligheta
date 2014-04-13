package net.leiligheta.samsung;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class ImageBuilder {
	private BufferedCanvas canvas;
	private ProcessBuilder pb;
	
	public ImageBuilder() {
		canvas = new BufferedCanvas(800, 480, BufferedImage.TYPE_INT_RGB);
		pb = new ProcessBuilder("sudo", "/usr/bin/frameCTRL.py", "monitor.jpg");
	}
	
	private void createImage() {
		canvas.setBackground(new GradientPaint(0,0, Color.BLUE, 0, 480, new Color(33,33,66)));
		Path path = new Path();
		path.setStartPoint(0, 240);
		path.setStrokeThickness(3);
		path.setStrokePaint(Color.YELLOW);
		for (int i = 0; i <= 800; i += 8) {
			path.addPoint(i, 240 + (int) (Math.sin(i * 2 * Math.PI / 800) * 200));
		}
		canvas.add(path);
		canvas.draw();
		
//		Polygon pol = new Polygon();
//		pol.addPoint(0, 240);
//		for (int i = 0; i < 800; i++) {
//			pol.addPoint(i, 240 + (int) (Math.sin(i * 2 * Math.PI / 800) * 200));
//		}
//		g.setPaint(new GradientPaint(0,0, Color.RED, 800, 0, Color.GREEN));
//		g.fillPolygon(pol);
//		g.setPaint(Color.WHITE);
//		g.drawPolygon(pol);
//		g.setPaint(Color.BLACK);
//		//g.fillRect(0, 0, 800, 480);
		
		
	}
	
	private void writeImage() {
		try {
			createImage();
			//ImageIO.write(image, "jpg", pb.start().getOutputStream());
			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
			ImageWriter writer = (ImageWriter) iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(1);
			writer.setOutput(new FileImageOutputStream(new File("./monitor.jpg")));
			IIOImage image = new IIOImage(canvas.getImage(), null, null);
			writer.write(null, image, iwp);
			writer.dispose();
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ImageBuilder ib = new ImageBuilder();
		ib.writeImage();		
	}

}
