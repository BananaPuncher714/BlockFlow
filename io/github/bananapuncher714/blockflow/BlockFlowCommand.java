package io.github.bananapuncher714.blockflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import io.github.bananapuncher714.blockflow.api.canvas.OverlaySubcanvas;
import io.github.bananapuncher714.blockflow.api.canvas.WorldSubcanvas;
import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.CategoryComparatorString;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;

public class BlockFlowCommand implements CommandExecutor, TabCompleter {
	protected BlockFlow plugin;
	
	public BlockFlowCommand( BlockFlow plugin ) {
		this.plugin = plugin;
	}
	
	@Override
	public List< String > onTabComplete( CommandSender sender, Command command, String label, String[] args ) {
		List< String > completions = new ArrayList< String >();
		List< String > suggestions = new ArrayList< String >();
		if ( args.length == 1 ) {
			suggestions.add( "create" );
			suggestions.add( "deselect" );
			suggestions.add( "select" );
			suggestions.add( "clear" );
			suggestions.add( "show" );
		} else if ( args.length == 2 ) {
			if ( args[ 0 ].equalsIgnoreCase( "select" ) ) {
				suggestions.addAll( plugin.getPolys().keySet() );
			}
		} else if ( args.length == 3 ) {
			if ( args[ 0 ].equalsIgnoreCase( "select" ) ) {
				suggestions.addAll( plugin.getDb().getValuesFor( new Category( "Year" ) ) );
			}
		}
		StringUtil.copyPartialMatches( args[ args.length - 1 ], suggestions, completions);
		Collections.sort( completions );
		return completions;
	}

	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		try {
			if ( args.length == 0 ) {
				help( sender, args );
			} else if ( args.length > 0 ) {
				String option = args[ 0 ];
				args = pop( args );
				if ( option.equalsIgnoreCase( "create" ) ) {
					create( sender, args );
				} else if ( option.equalsIgnoreCase( "select" ) ) {
					select( sender, args );
				} else if ( option.equalsIgnoreCase( "deselect" ) ) {
					deselect( sender, args );
				} else if ( option.equalsIgnoreCase( "clear" ) ) {
					clear( sender, args );
				} else if ( option.equalsIgnoreCase( "show" ) ) {
				}
			}
		} catch ( IllegalArgumentException exception ) {
			sender.sendMessage( exception.getMessage() );
		}
		return false;
	}

	private void help( CommandSender sender, String[] args ) {
		sender.sendMessage( ChatColor.AQUA + "Usage: /blockflow <create>" );
	}
	
	private void create( CommandSender sender, String[] args ) {
		Validate.isTrue( sender instanceof Player, ChatColor.RED + "Sender must be a player!" );
		Player player = ( Player ) sender;
		Canvas canvas = new Canvas( player.getWorld() );
		canvas.setLeast( player.getLocation().getBlock().getLocation().toVector() );
		canvas.setMost( player.getLocation().getBlock().getLocation().toVector().add( new Vector( 0, 1, 0 ) ) );
		
		plugin.setMainCanvas( canvas );
		canvas.clear();
		
		Vector bottom = canvas.getMost().clone().subtract( canvas.getLeast() );
		bottom.setY( 1 );
		
		WorldSubcanvas worldSubcanvas = new WorldSubcanvas( plugin.getPolys(), bottom );
		OverlaySubcanvas overlaySubcanvas = new OverlaySubcanvas( plugin, worldSubcanvas );
		
		canvas.registerSubcanvas( worldSubcanvas, canvas.getLeast() );
		canvas.registerSubcanvas( overlaySubcanvas, canvas.getLeast().clone().add( new Vector( 0, 1, 0 ) ) );

		plugin.setOverlaySubcanvas( overlaySubcanvas );
		plugin.setWorldSubcanvas( worldSubcanvas );
		
		canvas.forceUpdate();
		
		sender.sendMessage( ChatColor.GREEN + "Canvas created!" );
	}
	
	private void clear( CommandSender sender, String[] args ) {
		Canvas canvas = plugin.getMainCanvas();
		Validate.isTrue( canvas != null, ChatColor.RED + "A canvas has not been created yet!" );
		
		plugin.getOverlaySubcanvas().clear();
		
		sender.sendMessage( ChatColor.GREEN + "The board has been cleared!" );
	}
	
	private void select( CommandSender sender, String[] args ) {
		Canvas canvas = plugin.getMainCanvas();
		Validate.isTrue( canvas != null, ChatColor.RED + "A canvas has not been created yet!" );
		Validate.isTrue( args.length == 2, ChatColor.RED + "You must provide a region and a year to highlight!" );
		Validate.isTrue( plugin.getPolys().containsKey( args[ 0 ].toUpperCase() ), ChatColor.RED + "That is not a valid region!" );
		Map< Category, CategoryComparator > constraints = new HashMap< Category, CategoryComparator >();
		constraints.put( new Category( "Year" ), new CategoryComparatorString( args[ 1 ] ) );
		Validate.isTrue( plugin.getDb().getEntries( constraints ).size() > 0, ChatColor.RED + "There is no data recorded for this year!" );
		
		plugin.getOverlaySubcanvas().show( null, "0" );
		plugin.getOverlaySubcanvas().show( args[ 0 ].toUpperCase(), args[ 1 ] );
		
		sender.sendMessage( ChatColor.GREEN + args[ 0 ] + " has been selected" );
	}
	
	private void deselect( CommandSender sender, String[] args ) {
		Canvas canvas = plugin.getMainCanvas();
		Validate.isTrue( canvas != null, ChatColor.RED + "A canvas has not been created yet!" );
		
		plugin.getOverlaySubcanvas().show( null, "0" );
		
		sender.sendMessage( ChatColor.GREEN + "Deselected the current country!" );
	}
	
	private final String[] pop( String[] array ) {
		String[] array2 = new String[ Math.max( 0, array.length - 1 ) ];
		for ( int i = 1; i < array.length; i++ ) {
			array2[ i - 1 ] = array[ i ];
		}
		return array2;
	}
}
