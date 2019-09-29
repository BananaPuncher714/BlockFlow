package io.github.bananapuncher714.blockflow.api.canvas;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import io.github.bananapuncher714.blockflow.BlockFlow;
import io.github.bananapuncher714.blockflow.Subcanvas;
import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.api.poly.VectorLine;
import io.github.bananapuncher714.blockflow.file.PolyUtil;

public class WorldSubcanvas extends Subcanvas {
	protected Map< String, FlatPoly > polyMapper = new HashMap< String, FlatPoly >();
	protected Map< FlatPoly, Set< Location > > polys = new HashMap< FlatPoly, Set< Location > >();
	protected Map< Location, Material > locations = new HashMap< Location, Material >();
	protected Vector size = new Vector( 0, 0, 0 );
	protected BukkitTask task;
	
	public WorldSubcanvas( Map< String, FlatPoly > polys, Vector size ) {
		super( size );
		for ( String name : polys.keySet() ) {
			FlatPoly poly = polys.get( name );
			FlatPoly rescaled = poly.getScaled( .15 );
			this.polys.put( rescaled, new HashSet< Location >() );
			polyMapper.put( name, rescaled );
			
			for ( VectorLine line : rescaled.getLines() ) {
				this.size.setX( Math.max( this.size.getX(), Math.max( line.getMax().getX(), line.getMin().getX() ) ) );
				this.size.setZ( Math.max( this.size.getZ(), Math.max( line.getMax().getZ(), line.getMin().getZ() ) ) );
			}
		}
		this.setSize( this.size );
	}

	private void tick() {
		for ( int i = 0; i < 50; i++ ) {
			if ( locations.isEmpty() ) {
				break;
			}
			Location location = locations.keySet().iterator().next();
			Material mat = locations.remove( location );
			Block block = location.getBlock();
			if ( block.getType() == mat ) {
				i--;
				continue;
			}
			location.getBlock().setType( mat, false );
		}
	}
	
	@Override
	public void fill( World world, Vector leastCorner ) {
		for ( int x = 0; x < getSize().getX(); x++ ) {
			for ( int y = 0; y < getSize().getZ(); y++ ) {
				Location newLoc = getBlockLocation( new Location( world, 0, 0, 0 ).add( leastCorner.clone().add( new Vector( x, 0, y ) ) ) );
				locations.put( newLoc, Material.WHITE_WOOL );
				for ( FlatPoly poly : polys.keySet() ) {
					if ( PolyUtil.contains( poly, newLoc.toVector().subtract( leastCorner ) ) ) {
						polys.get( poly ).add( newLoc );
						locations.put( newLoc, Material.GLASS );
					}
				}
			}
		}
		for ( FlatPoly poly : polys.keySet() ) {
			for ( VectorLine line : poly.getLines() ) {
				Vector min = line.getMin();
				Vector max = line.getMax();
				Vector direction = max.clone().subtract( min );
				
				double s = Math.max( Math.abs( direction.getX() ), Math.abs( direction.getZ() ) );
				for ( double i = 0; i <= s; i++ ) {
					Location loc = new Location( world, ( int ) ( max.getX() + ( min.getX() - max.getX() ) * ( i / s ) + leastCorner.getX() ), leastCorner.getY(), ( int ) ( max.getZ() + ( min.getZ() - max.getZ() ) * ( i / s ) + leastCorner.getZ() ) );
					locations.put( getBlockLocation( loc ), Material.BLACK_WOOL );
				}
			}
		}
		task = Bukkit.getScheduler().runTaskTimer( JavaPlugin.getPlugin( BlockFlow.class ), this::tick, 0, 4 );
	}

	public void disable() {
		if ( task != null ) {
			task.cancel();
		}
		locations.clear();
	}
	
	public Set< Location > getPolyLocs( String id ) {
		return polys.get( polyMapper.get( id ) );
	}
	
	public final static Location getBlockLocation( Location loc ) {
		return new Location( loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() );
	}
}
