package io.github.bananapuncher714.blockflow.api.db;

import java.util.Collection;

public class DBEntryBubbleSort implements DBEntrySorter {
	protected Category category;
	
	public DBEntryBubbleSort( Category category ) {
		this.category = category;
	}
	
	@Override
	public DBEntry[] sort( Collection< DBEntry > entries ) {
		DBEntry[] entryArr = entries.toArray( new DBEntry[ entries.size() ] );
		
		bubbleSort( entryArr );
		
		return entryArr;
	}

	private void bubbleSort( DBEntry[] array ) {
	    boolean sorted = false;
	    DBEntry temp;
	    while( !sorted ) {
	        sorted = true;
	        for ( int i = 0; i < array.length - 1; i++ ) {
	            if ( array[ i ].getDouble( category ) > array[ i + 1 ].getDouble( category ) ) {
	            	temp = array[ i ];
	                array[ i ] = array[ i + 1 ];
	                array[ i + 1 ] = temp;
	                sorted = false;
	            }
	        }
	    }
	}
}
