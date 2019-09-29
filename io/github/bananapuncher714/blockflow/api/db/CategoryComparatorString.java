package io.github.bananapuncher714.blockflow.api.db;

import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;

public class CategoryComparatorString implements CategoryComparator {
	protected String str;
	
	public CategoryComparatorString( String str ) {
		this.str = str;
	}
	
	@Override
	public boolean compare( Category category, DBEntry value ) {
		return value.get( category ).equals( str );
	}
}
