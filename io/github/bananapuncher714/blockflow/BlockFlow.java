package io.github.bananapuncher714.blockflow;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;

public class BlockFlow extends JavaPlugin {
	private Canvas mainCanvas;
	private FlatFileDB db;
	private WorldEditPlugin worldedit;
	
	@Override
	public void onEnable() {
		worldedit = ( WorldEditPlugin ) Bukkit.getPluginManager().getPlugin( "WorldEdit" );
	}

	public Canvas getMainCanvas() {
		return mainCanvas;
	}

	public void setMainCanvas( Canvas mainCanvas ) {
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
}
