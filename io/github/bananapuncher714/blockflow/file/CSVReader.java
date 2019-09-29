package io.github.bananapuncher714.blockflow.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			boolean first = true;
			Category[] headers = null;
			FlatFileDB db = null;
			
			String line;
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
						if ( i >= contents.length ) {
							entry.set( headers[ i ], "" );
						} else {
							entry.set( headers[ i ], contents[ i ] );
						}
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
	
	public static void save( FlatFileDB db, File file ) {
		if ( file.exists() ) {
			file.delete();
		}
		try {
			file.createNewFile();
			
			FileWriter fileWriter = new FileWriter( file );
			BufferedWriter writer = new BufferedWriter( fileWriter );
			
			List< Category > categories = new ArrayList< Category >( db.getCategories() );
			
			for ( int i = 0; i < categories.size(); i++ ) {
				writer.write( categories.get( i ).getIdentifier() );
				if ( i < categories.size() - 1 ) {
					writer.write( "," );
				}
			}
			writer.write( "\n" );
			
			for ( DBEntry entry : db.getEntries() ) {
				for ( int i = 0; i < categories.size(); i++ ) {
					writer.write( entry.get( categories.get( i ) ) );
					if ( i < categories.size() - 1 ) {
						writer.write( "," );
					}
				}
				writer.write( "\n" );
			}
			
		    writer.close();
		    fileWriter.close();
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
		
	}
}
