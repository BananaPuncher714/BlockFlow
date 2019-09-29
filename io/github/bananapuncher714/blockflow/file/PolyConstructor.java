package io.github.bananapuncher714.blockflow.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;

public class PolyConstructor {
	public static Map< String, FlatPoly > getPolys( File file ) {
		if ( !file.exists() ) {
			throw new IllegalArgumentException( "File does not exist!" );
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration( file );
		Map< String, FlatPoly > polys = new HashMap< String, FlatPoly >();
		for ( String key : config.getKeys( false ) ) {
			String val = config.getString( key );
			System.out.println( key );
			FlatPoly poly = new FlatPoly( val );
			polys.put( key, poly );
		}
		
		return polys;
	}
}
