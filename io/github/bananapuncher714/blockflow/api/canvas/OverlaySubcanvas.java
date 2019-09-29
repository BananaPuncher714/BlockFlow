package io.github.bananapuncher714.blockflow.api.canvas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import io.github.bananapuncher714.blockflow.BlockFlow;
import io.github.bananapuncher714.blockflow.Subcanvas;
import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.CategoryComparatorString;
import io.github.bananapuncher714.blockflow.api.db.DBEntry;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;
import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;

public class OverlaySubcanvas extends Subcanvas {
	protected Map< Location, Material > blocks = new HashMap< Location, Material >();
	protected Set< Location > decay = new HashSet< Location >();
	protected World world;
	protected Vector leastCorner;
	protected WorldSubcanvas canvas;
	
	protected Map< Category, CategoryComparator > constraints = new HashMap< Category, CategoryComparator >();
	
	protected String current = null;
	
	protected BlockFlow plugin;
	
	public OverlaySubcanvas( BlockFlow plugin, WorldSubcanvas subcanvas ) {
		super( subcanvas.getSize() );
		canvas = subcanvas;
		this.plugin = plugin;
	}
	
	@Override
	public void fill( World world, Vector leastCorner ) {
		this.world = world;
		this.leastCorner = leastCorner;
	}
	
	public void show( String region, String year ) {
		current = region;
		if ( region == null ) {
			for ( Location location : decay ) {
				canvas.locations.put( location, Material.GLASS );
			}
			constraints.clear();
		} else {
			constraints.put( new Category( "Year" ), new CategoryComparatorString( year ) );
			constraints.put( new Category( "Country Code" ), new CategoryComparatorString( region ) );
			FlatPoly poly = plugin.getPolys().get( region );
			if ( poly == null ) {
				throw new IllegalArgumentException( "Incorrect poly!" );
			}
			
			// Time to display the stats
			Set< DBEntry > entries = plugin.getDb().getEntries( constraints );
			if ( entries.size() > 1 ) {
				System.out.println( "Broken!" );
			}
			DBEntry entry = entries.iterator().next();
			
			double popHeight = 80 * ( entry.getDouble( new Category( "Population" ) ) - plugin.getPopMin() ) / plugin.getPopMax();
			double mHeight = entry.getDouble( new Category( "Mean men height(cm)" ) ) / 3.0;
			double fHeight = entry.getDouble( new Category( "Mean women height(cm)" ) ) / 3.0;
			
			System.out.println( popHeight + ":" + mHeight + ":" + fHeight );
			
			Set< Location > polyLocs = canvas.getPolyLocs( region );
			for ( Location location : polyLocs ) {
				if ( location.getBlock().getType() == Material.GLASS ) {
					canvas.locations.put( location.clone().add( 0, popHeight, 0 ), Material.GREEN_STAINED_GLASS );
					blocks.put( location.clone().add( 0, popHeight, 0 ), Material.AIR );
					canvas.locations.put( location.clone().add( 0, mHeight, 0 ), Material.BLUE_STAINED_GLASS );
					blocks.put( location.clone().add( 0, mHeight, 0 ), Material.AIR );
					canvas.locations.put( location.clone().add( 0, fHeight, 0 ), Material.RED_STAINED_GLASS );
					blocks.put( location.clone().add( 0, fHeight, 0 ), Material.AIR );
					
					canvas.locations.put( location, Material.GLOWSTONE );
					decay.add( location );
				}
			}
		}
	}
	
	public void clear() {
		for ( Location location : blocks.keySet() ) {
			canvas.locations.put( location, blocks.get( location ) );
		}
		show( null, "0" );
	}

	public String getCurrent() {
		return current;
	}
	
	@Override
	public void disable() {
		blocks.clear();
	}
}
