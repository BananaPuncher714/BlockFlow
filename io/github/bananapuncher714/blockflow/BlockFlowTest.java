package io.github.bananapuncher714.blockflow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.DBEntry;
import io.github.bananapuncher714.blockflow.api.db.DBEntryBubbleSort;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;
import io.github.bananapuncher714.blockflow.api.db.StringCategoryComparator;
import io.github.bananapuncher714.blockflow.file.CSVReader;

public class BlockFlowTest {
	private static final File baseDir = new File( System.getProperty( "user.dir" ) );
	
	public static void main( String[] args ) {
		File csv = new File( baseDir + "/csv/" + "average-height-of-men.csv" );
		FlatFileDB db = CSVReader.read( csv );
		print( db );
	}
	
	private static void print( FlatFileDB db ) {
		List< Category > categories = new ArrayList< Category >( db.getCategories() );
		for ( Category category : categories ) {
			System.out.print( category.getIdentifier() + "\t" );
		}
		System.out.println();
		int limit = 50;
		Map< Category, CategoryComparator > map = new HashMap< Category, CategoryComparator >();
		map.put( new Category( "Entity" ), new StringCategoryComparator( "Afghanistan" ) );
		Set< DBEntry > entrySet = db.getEntries( map );
		DBEntry[] entryArr = new DBEntryBubbleSort( new Category( "Year" ) ).sort( entrySet );
		for ( DBEntry entry : entryArr ) {
			for ( Category category : categories ) {
				System.out.print( entry.get( category ) + "\t" );
			}
			System.out.println();
			if ( limit-- <= 0 ) {
				break;
			}
		}
	}
}
