package io.github.bananapuncher714.blockflow.api.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlatFileDB {
	protected final Set< DataValue< ? > > values = new HashSet< DataValue< ? > >();
	protected final Set< Category > categories;
	
	public FlatFileDB( Category[] categories ) {
		this.categories = Collections.unmodifiableSet( new HashSet< Category >( Arrays.asList( categories ) ) );
	}
	
	public Set< Category > getCategories() {
		return categories;
	}
	
	public void add( DataValue< ? > value ) {
		for ( Category cat : categories  ) {
			if ( value.get( cat ) == null ) {
				throw new IllegalArgumentException( "Value provided is missing the category " + cat );
			}
		}
		values.add( value );
	}
	
	public Set< DataValue< ? > > get( Map< Category, String > categories ) {
		for ( Category cat : categories.keySet() ) {
			if ( !this.categories.contains( cat ) ) {
				throw new IllegalArgumentException( "Database does not contain this category!" );
			}
		}
		
		Set< DataValue< ? > > matches = new HashSet< DataValue< ? > >();
		for ( DataValue< ? > value : values ) {
			boolean valid = true;
			for ( Category category : categories.keySet() ) {
				String expected = categories.get( category );
				if ( !value.get( category ).equals( expected ) ) {
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
}
