package io.github.bananapuncher714.blockflow.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.DBEntry;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;

public class CSVReader {
	private CSVReader() {
	}
	
	public static FlatFileDB read( File csvFile ) {
		if ( !csvFile.exists() ) {
			throw new IllegalArgumentException( "File does not exist!" );
		}
		try (
				FileReader fReader = new FileReader( csvFile );
				BufferedReader reader = new BufferedReader( fReader ); ) {
			String line;
			boolean first = true;
			Category[] headers = null;
			FlatFileDB db = null;
			while ( ( line = reader.readLine() ) != null ) {
				String[] contents = line.split( "," );
				if ( first ) {
					first = false;
					headers = new Category[ contents.length ];
					for ( int i = 0; i < headers.length; i++ ) {
						String header = contents[ i ];
						Category newCategory = new Category( header );
						headers[ i ] = newCategory;
					}
					db = new FlatFileDB( headers );
				} else {
					DBEntry entry = new DBEntry();
					for ( int i = 0; i < headers.length; i++ ) {
						entry.set( headers[ i ], contents[ i ] );
					}
					db.add( entry );
				}
			}
			return db;
		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		} 
	}
}
