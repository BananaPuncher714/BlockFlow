package io.github.bananapuncher714.blockflow.api.poly;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.util.Vector;

public class FlatPoly {
	Set< VectorLine > lines = new HashSet< VectorLine >();
	
	protected Vector min;
	protected Vector max;
	
	public FlatPoly() {
	}

	public void add( VectorLine line ) {
		if ( min == null || max == null ) {
			min = line.getMin().clone();
			max = line.getMax().clone();
		}
		min.setX( Math.min( line.getMax().getX(), Math.min( line.getMin().getX(), min.getX() ) ) );
		min.setZ( Math.min( line.getMax().getZ(), Math.min( line.getMin().getZ(), min.getZ() ) ) );
		
		max.setX( Math.max( line.getMax().getX(), Math.max( line.getMin().getX(), max.getX() ) ) );
		max.setZ( Math.max( line.getMax().getZ(), Math.max( line.getMin().getZ(), max.getZ() ) ) );
		
		lines.add( line );
	}
	
	public Set< VectorLine > getLines() {
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
