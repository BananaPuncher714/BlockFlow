package io.github.bananapuncher714.blockflow;

import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class PaintPanel extends JPanel {
	protected int windowWidth = 500;
	protected int windowHeight = 500;
	
	private int offsetX = 20;
	private int offsetY = 20;
	
	private Graphics g;
	
	private double scale = .5;
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		this.g = g;
	}
	
	public void drawLine( double x1, double y1, double x2, double y2 ) {
		double s = Math.max( Math.abs( x1 - x2 ), Math.abs( y1 - y2 ) ) * scale;
		for ( double i = 0; i <= s; i++ ) {
			drawPoint( x2 + ( x1 - x2 ) * ( i / s ), y2 + ( y1 - y2 ) * ( i / s ) );
		}
	}
	
	public void drawPoint( double x, double y ) {
		g.fillRect( ( int ) ( offsetX + x * scale ), ( int ) ( offsetY + y * scale ), 1, 1 );
	}
}
