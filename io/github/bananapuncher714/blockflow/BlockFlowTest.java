package io.github.bananapuncher714.blockflow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.CategoryComparatorString;
import io.github.bananapuncher714.blockflow.api.db.DBEntry;
import io.github.bananapuncher714.blockflow.api.db.DBEntryBubbleSort;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;
import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.file.CSVReader;
import io.github.bananapuncher714.blockflow.file.PolyUtil;

public class BlockFlowTest {
	private static final File baseDir = new File( System.getProperty( "user.dir" ) );
	
	public static void main( String[] args ) {
		File csv = new File( baseDir + "/csv/" + "uberdb.csv" );
		FlatFileDB db = CSVReader.read( csv );
		
		File polys = new File( baseDir + "/data/" + "poly.dat" );
		
		Map< String, FlatPoly > polyMap = PolyUtil.load( polys );

		File polyFile = new File( baseDir + "/data/" + "test-poly.dat" );
		PolyUtil.save( polyMap, polyFile );
		
		ArrayList< FlatPoly > polyList = new ArrayList< FlatPoly >( polyMap.values() );
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				new WorldPanel( polyList );
			}
		} );
	}
	
	private static void print( FlatFileDB db ) {
		List< Category > categories = new ArrayList< Category >( db.getCategories() );
		for ( Category category : categories ) {
			System.out.print( category.getIdentifier() + "\t" );
		}
		System.out.println();
		int limit = 50;
		Map< Category, CategoryComparator > map = new HashMap< Category, CategoryComparator >();
		map.put( new Category( "Entity" ), new CategoryComparatorString( "Afghanistan" ) );
		Set< DBEntry > entrySet = db.getEntries( map );
		DBEntry[] entryArr = new DBEntryBubbleSort( new Category( "Mean male height (cm) (centimeters)" ) ).sort( entrySet );
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
