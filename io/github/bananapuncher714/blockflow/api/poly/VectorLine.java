package io.github.bananapuncher714.blockflow.api.poly;

import org.bukkit.util.Vector;

public class VectorLine {
	protected Vector min;
	protected Vector max;
	
	public VectorLine( Vector min, Vector max ) {
		this.min = min.clone();
		this.max = max.clone();
	}

	public Vector getMin() {
		return min;
	}

	public void setMin( Vector min ) {
		this.min = min;
	}

	public Vector getMax() {
		return max;
	}

	public void setMax( Vector max ) {
		this.max = max;
	}

	public boolean intersects( VectorLine other ) {
		Vector line = max.clone().subtract( min );
		
		return false;
	}
}
