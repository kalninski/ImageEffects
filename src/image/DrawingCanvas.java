package image;

import java.awt.*;// for Color and Graphics classes
import java.awt.geom.*;//for creating shapes and paths
import javax.swing.*;

public class DrawingCanvas extends JComponent {
	
	private int width;
	private int height;
	
	public DrawingCanvas(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D.Double r = new Rectangle2D.Double(0, 0, width, height);
		g2d.setColor(new Color(50, 80, 200));
		g2d.fill(r);
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

g2d.addRenderingHints(rh);
		
		Ellipse2D.Double e = new Ellipse2D.Double(0, 0, 300, 300);
		g2d.setColor(new Color(255, 255, 255));
		g2d.fill(e);
		
		Line2D.Double l = new Line2D.Double(500, 500, 650, 500);
		g2d.setColor(new Color(0, 0, 0));
		g2d.draw(l);
		
		QuadCurve2D.Double c = new QuadCurve2D.Double(130, 130, 200, 200, 130, 270);
		g2d.setColor(new Color(0,0,0));
		g2d.draw(c);
		
		
	}
}
