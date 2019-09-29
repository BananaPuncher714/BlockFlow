package io.github.bananapuncher714.blockflow.api.poly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.util.Vector;

public class FlatPoly {
	Set< VectorLine > lines = new HashSet< VectorLine >();
	
	protected Vector min;
	protected Vector max;
	
	public FlatPoly( String svg ) {
		Vector start = new Vector( 0, 0, 0 );
		Vector cursor = new Vector( 0, 0, 0 );
		boolean m = false;
		boolean M = false;
		boolean z = false;
		boolean l = false;
		for ( String val : svg.split( "\\s" ) ) {
			if ( val.isEmpty() ) {
				continue;
			}
			if ( !( m || M || l ) ) {
				m = val.equals( "m" );
				M = val.equals( "M" );
				z = val.equals( "z" );
				l = val.equals( "l" );
			}
			if ( val.equals( "m" ) || val.equals( "M" ) || val.equals( "l" ) ) {
				continue;
			}
			if ( z ) {
				lines.add( new VectorLine( cursor.clone(), start.clone() ) );
				continue;
			}
			
			String[] coords = val.split( "," );
			double x = Double.parseDouble( coords[ 0 ] );
			double y = Double.parseDouble( coords[ 1 ] );
			
			if ( m ) {
				cursor.add( new Vector( x, 0.0, y ) );
				start = cursor.clone();
			} else if ( M ) {
				cursor.setX( x );
				cursor.setZ( y );
				start = cursor.clone();
			} else if ( l ) {
				Vector old = cursor.clone();
				cursor.add( new Vector( x, 0.0, y ) );
				start = cursor.clone();
				lines.add( new VectorLine( old, cursor.clone() ) );
			} else {
				Vector old = cursor.clone();
				cursor.add( new Vector( x, 0.0, y ) );
				lines.add( new VectorLine( old, cursor.clone() ) );
			}
			
			m = false;
			M = false;
			z = false;
			l = false;
			
			if ( min == null || max == null ) {
				min = cursor.clone();
				max = cursor.clone();
			}
			min.setX( Math.min( cursor.getX(), min.getX() ) );
			min.setZ( Math.min( cursor.getZ(), min.getZ() ) );
			max.setX( Math.max( cursor.getX(), max.getX() ) );
			max.setZ( Math.max( cursor.getZ(), max.getZ() ) );
				
		}
	}
	
	public Set<VectorLine> getLines() {
		return lines;
	}

	public Vector getMin() {
		return min;
	}

	public Vector getMax() {
		return max;
	}

	public boolean inside( Vector point ) {
		return false;
	}
}
