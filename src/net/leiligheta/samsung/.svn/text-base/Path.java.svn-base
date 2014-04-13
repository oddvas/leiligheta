package net.leiligheta.samsung;

import java.util.LinkedList;
import java.util.ListIterator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;

public class Path implements Drawable {
	private Point startPoint;
	private LinkedList<Point> points;
	private Integer strokeThickness;
	private Boolean closed = false;
	private Boolean fill = false;
	private Paint strokePaint;
	private Paint fillPaint;
	
	public Path() {
		startPoint = new Point(0, 0);
		points = new LinkedList<Point>();
		strokePaint = Color.WHITE;
	}
	
	public void draw(Graphics2D g) {
		if (fill && fillPaint != null) {
			Polygon pol = new Polygon();
			pol.addPoint(startPoint.x, startPoint.y);
			for (Point p : points) {
				pol.addPoint(p.x, p.y);
			}
			closed = true;
			g.setPaint(fillPaint);
			g.fillPolygon(pol);
			
		}
		if (strokeThickness != null) g.setStroke(new BasicStroke(strokeThickness));
		g.setPaint(strokePaint);
		ListIterator<Point> i = points.listIterator();
		Point lastPoint = startPoint;
		Point currentPoint = i.next();
		g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
		while (i.hasNext()) {
			lastPoint = currentPoint;
			currentPoint = i.next();
			g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
		}
		if (closed) {
			g.drawLine(points.getLast().x, points.getLast().y, startPoint.x, startPoint.y);
		}
	}
	
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}
	
	public void setStartPoint(int x, int y) {
		this.startPoint = new Point(x, y);
	}
	
	public void setStrokeThickness(Integer strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public void setFill(Boolean fill) {
		this.fill = fill;
	}

	public void setStrokePaint(Paint strokePaint) {
		this.strokePaint = strokePaint;
	}

	public void setFillPaint(Paint fillPaint) {
		this.fillPaint = fillPaint;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public void clear() {
		points.clear();
	}
}
