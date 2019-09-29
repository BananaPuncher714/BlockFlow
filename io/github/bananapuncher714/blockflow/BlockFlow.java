package io.github.bananapuncher714.blockflow;

import java.io.File;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;
import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.file.CSVReader;
import io.github.bananapuncher714.blockflow.file.PolyUtil;
import io.github.bananapuncher714.blockflow.util.FileUtil;

public class BlockFlow extends JavaPlugin {
	private Canvas mainCanvas;
	private FlatFileDB db;
	private WorldEditPlugin worldedit;
	
	private BlockFlowCommand command;
	
	private Map< String, FlatPoly > polys;
	
	private File CSV_FILE;
	private File POLY_FILE;
	
	
	
	@Override
	public void onEnable() {
		CSV_FILE = new File( getDataFolder() + "/" + "uberdb.csv" );
		POLY_FILE = new File( getDataFolder() + "/" + "poly.dat" );
		
		init();
		
		load();
		
		worldedit = ( WorldEditPlugin ) Bukkit.getPluginManager().getPlugin( "WorldEdit" );
		
		command = new BlockFlowCommand( this );
		
		getCommand( "blockflow" ).setExecutor( command );
		getCommand( "blockflow" ).setTabCompleter( command );
	}
	
	@Override
	public void onDisable() {
		if ( mainCanvas != null ) {
			mainCanvas.disable();
		}
	}
	
	private void init() {
		FileUtil.saveToFile( getResource( "data/uberdb.csv" ), CSV_FILE, false );
		FileUtil.saveToFile( getResource( "data/poly.dat" ), POLY_FILE, false );
	}

	private void load() {
		 db = CSVReader.read( CSV_FILE );
		 getLogger().info( "Uber DB Loaded!" );
		 polys = PolyUtil.load( POLY_FILE );
		 getLogger().info( "Polys loaded!" );
	}
	
	public Canvas getMainCanvas() {
		return mainCanvas;
	}

	public void setMainCanvas( Canvas mainCanvas ) {
		if ( mainCanvas != null  ) {
			mainCanvas.disable();
		}
		this.mainCanvas = mainCanvas;
	}
	
	/**
	 * Get a canvas from a player's selection
	 * 
	 * @param player
	 * @return
	 * A canvas, or null if they don't have anything selected with the WorldEdit API
	 */
	public Canvas getSelection( Player player ) {
		try {
			Region region = worldedit.getSession( player ).getSelection( BukkitAdapter.adapt( player.getWorld() ) );
			if ( region == null ) {
				return null;
			}
			Location minimumPoint = BukkitAdapter.adapt( player.getWorld(), region.getMinimumPoint() );
	        Location maximumPoint = BukkitAdapter.adapt( player.getWorld(), region.getMaximumPoint() );
	        
	        Canvas canvas = new Canvas( player.getWorld() );
	        canvas.setMost( maximumPoint.toVector() );
	        canvas.setLeast( minimumPoint.toVector() );
			return canvas;
		} catch ( IncompleteRegionException e ) {
			return null;
		}
	}

	public FlatFileDB getDb() {
		return db;
	}

	public Map< String, FlatPoly > getPolys() {
		return polys;
	}
}
