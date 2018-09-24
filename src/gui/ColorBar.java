package gui;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Creates a JPanel colored with a gradient.
 * @author Severine Liegeois
 *
 */
public class ColorBar extends JPanel {
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new ColorBar.
	 */
	public ColorBar(){}
	
	/**
	 * Paints a rectangle containing a gradient from white to blue.
	 * @param g graphic context
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint p = new GradientPaint(0, 0, Color.WHITE, getWidth(), 0, Color.BLUE);
		g2d.setPaint(p);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
}