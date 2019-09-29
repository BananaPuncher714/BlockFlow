package io.github.bananapuncher714.blockflow.api.db;

import java.util.HashSet;
import java.util.Set;

public abstract class Category {
	private final Set< String > categories = new HashSet< String >();
	
	protected void register( String category ) {
		categories.add( category );
	}
	
    public final boolean contains( String string ) {
    	return categories.contains( string );
    }
}
