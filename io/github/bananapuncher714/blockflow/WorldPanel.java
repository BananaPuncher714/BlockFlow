package io.github.bananapuncher714.blockflow;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;

import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.api.poly.VectorLine;

public class WorldPanel extends PaintPanel {
	
	JFrame f;
	
	List< FlatPoly > polys;
	
	public WorldPanel( List< FlatPoly > polys ) {
		this.polys = polys;
		
		f = new JFrame( "Drawing Board" );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		f.add( this );
		
		f.setSize( windowWidth, windowHeight );
		f.setVisible( true );
		f.setResizable( true );
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		
		for ( FlatPoly poly : polys ) {
			for ( VectorLine line : poly.getLines() ) {
				this.drawLine( line.getMin().getX(), line.getMin().getZ(), line.getMax().getX(), line.getMax().getZ() );
			}
		}
	}
}
