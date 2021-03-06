package io.github.bananapuncher714.blockflow;

import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class Subcanvas {
	Vector size;
	
	public Subcanvas( Vector size ) {
		setSize( size );
	}
	
	public Vector getSize() {
		return size;
	}

	public void setSize( Vector size ) {
		this.size = size.clone();
	}

	public abstract void fill( World world, Vector leastCorner );
	public abstract void disable();
}
