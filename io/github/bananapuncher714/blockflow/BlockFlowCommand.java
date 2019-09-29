package io.github.bananapuncher714.blockflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

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
		Canvas canvas = plugin.getSelection( player );
		Validate.notNull( canvas, ChatColor.RED + "You must make a selection first!" );
		
		plugin.setMainCanvas( canvas );
		canvas.clear();
	}
	
	private final String[] pop( String[] array ) {
		String[] array2 = new String[ Math.max( 0, array.length - 1 ) ];
		for ( int i = 1; i < array.length; i++ ) {
			array2[ i - 1 ] = array[ i ];
		}
		return array2;
	}
}
