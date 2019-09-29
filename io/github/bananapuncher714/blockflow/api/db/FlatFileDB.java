package io.github.bananapuncher714.blockflow.api.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlatFileDB {
	protected final Set< DBEntry > values = new HashSet< DBEntry >();
	protected final Set< Category > categories;
	
	public FlatFileDB( Set< Category > categories ) {
		this.categories = Collections.unmodifiableSet( categories );
	}
	
	public FlatFileDB( Category... categories ) {
		this.categories = Collections.unmodifiableSet( new HashSet< Category >( Arrays.asList( categories ) ) );
	}
	
	public Set< Category > getCategories() {
		return categories;
	}
	
	public void add( DBEntry entry ) {
		for ( Category cat : categories  ) {
			if ( !entry.contains( cat ) ) {
				throw new IllegalArgumentException( "Value provided is missing the category " + cat );
			}
		}
		values.add( entry );
	}
	
	public Set< DBEntry > getEntries() {
		return values;
	}
	
	public Set< String > getValuesFor( Category category ) {
		Set< String > vals = new HashSet< String >();
		for ( DBEntry entry : values ) {
			vals.add( entry.get( category ) );
		}
		return vals;
	}
	
	public Set< DBEntry > getEntries( Map< Category, CategoryComparator > categories ) {
		for ( Category cat : categories.keySet() ) {
			if ( !this.categories.contains( cat ) ) {
				throw new IllegalArgumentException( "Database does not contain this category! " + cat.getIdentifier() );
			}
		}
		
		Set< DBEntry > matches = new HashSet< DBEntry >();
		for ( DBEntry value : values ) {
			boolean valid = true;
			for ( Category category : categories.keySet() ) {
				CategoryComparator comparator = categories.get( category );
				if ( !comparator.compare( category, value ) ) {
					valid = false;
					break;
				}
			}
			if ( valid ) {
				matches.add( value );
			}
		}
		
		return matches;
	}
	
	public static interface CategoryComparator {
		boolean compare( Category category, DBEntry value );
	}
}
