package io.github.bananapuncher714.blockflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Canvas {
	protected World world;
	protected Vector least;
	protected Vector most;
	
	protected Map< Subcanvas, Vector > subcanvases = new HashMap< Subcanvas, Vector >();
	
	public Canvas( World world ) {
		this.world = world;
	}

	public Canvas( World world, Vector min, Vector max ) {
		this( world );
		setLeast( min );
		setMost( max );
	}
	
	public Vector getLeast() {
		return least;
	}

	public void setLeast( Vector least ) {
		this.least = least.clone();
	}

	public Vector getMost() {
		return most;
	}

	public void setMost( Vector most ) {
		this.most = most.clone();
	}
	
	public void registerSubcanvas( Subcanvas canvas, Vector min ) {
		subcanvases.put( canvas, min.clone());
	}
	
	public Map< Subcanvas, Vector > getSubcanvases() {
		return subcanvases;
	}
	
	public void forceUpdate() {
		clear();
		// Update the area forcefully
		for ( Iterator< Entry< Subcanvas, Vector > > iterator = subcanvases.entrySet().iterator(); iterator.hasNext(); ) {
			Entry< Subcanvas, Vector > entry = iterator.next();
			Subcanvas canvas = entry.getKey();
			Vector minPos = entry.getValue();
			
			canvas.fill( minPos );
		}
	}
	
	public void clear() {
		for ( int x = least.getBlockX(); x <= most.getBlockX(); x++ ) {
			for ( int y = least.getBlockY(); y <= most.getBlockY(); y++ ) {
				for ( int z = least.getBlockZ(); z <= most.getBlockZ(); z++ ) {
					world.getBlockAt( x, y, z ).setType( Material.AIR, false );
				}
			}
		}
	}
}
