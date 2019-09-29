package io.github.bananapuncher714.blockflow.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.util.Vector;

import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.api.poly.VectorLine;

public class PolyUtil {
	public static Map< String, FlatPoly > load( File file ) {
		if ( !file.exists() ) {
			throw new IllegalArgumentException( "File does not exist!" );
		}
		Map< String, FlatPoly > polys = new HashMap< String, FlatPoly >();
		
		try (
				FileReader fReader = new FileReader( file );
				BufferedReader reader = new BufferedReader( fReader ); ) {
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				String[] contents = line.split( ":" );
				String country = contents[ 0 ];
				String[] lines = contents[ 1 ].split( "\\s" );
				FlatPoly poly = new FlatPoly();
				for ( String segment : lines ) {
					String[] coords = segment.split( "," );
					double minX = Double.parseDouble( coords[ 0 ] );
					double minZ = Double.parseDouble( coords[ 1 ] );
					double maxX = Double.parseDouble( coords[ 2 ] );
					double maxZ = Double.parseDouble( coords[ 3 ] );
					
					Vector min = new Vector( minX, 0, minZ );
					Vector max = new Vector( maxX, 0, maxZ );
					
					poly.add( new VectorLine( min, max ) );
				}
				polys.put( country, poly );
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return polys;
	}

	public static void save( Map< String, FlatPoly > polys, File file ) {
		if ( file.exists() ) {
			file.delete();
		}
		try {
			file.createNewFile();

			FileWriter fileWriter = new FileWriter( file );
			BufferedWriter writer = new BufferedWriter( fileWriter );

			for ( String country : polys.keySet() ) {
				FlatPoly poly = polys.get( country );
				Set< VectorLine > lines = poly.getLines();
				String fin = country + ":";
				for ( VectorLine line : lines ) {
					fin += line.getMin().getX() + "," + line.getMin().getZ() + "," + line.getMax().getX() + "," + line.getMax().getZ() + " ";
				}
				writer.write( fin + "\n" );
			}
			
			
			writer.close();
			fileWriter.close();
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}

	}
}
