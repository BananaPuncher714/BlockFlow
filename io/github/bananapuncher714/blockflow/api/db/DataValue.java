package io.github.bananapuncher714.blockflow.api.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Store something locally
 *
 * @param < T >
 * A value that can be interpreted and stored or something like that
 */
public class DataValue< T > {
	protected Map< Category, String > categories = new HashMap< Category, String >();
	protected T value;
	
	public DataValue( T value ) {
		this.value = value;
	}
	
	public void setCategory( Category category, String value ) {
		if ( !category.contains( value ) ) {
			throw new IllegalArgumentException( "Category value invalid!" );
		}
		categories.put( category, value );
	}
	
	public T getValue() {
		return value;
	}
	
	public String get( Category category ) {
		return categories.get( category );
	}
}
