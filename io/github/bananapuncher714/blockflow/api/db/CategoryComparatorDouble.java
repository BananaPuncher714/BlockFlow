package io.github.bananapuncher714.blockflow.api.db;

import io.github.bananapuncher714.blockflow.api.db.FlatFileDB.CategoryComparator;

public class CategoryComparatorDouble implements CategoryComparator {
	protected boolean lessThan;
	protected double val;
	
	public CategoryComparatorDouble( double val, boolean lessThan ) {
		this.val = val;
		this.lessThan = lessThan;
	}
	
	@Override
	public boolean compare( Category category, DBEntry value ) {
		String val = value.get( category );
		if ( val == null || val.isEmpty() ) {
			return false;
		}
		double num = Double.valueOf( val );
		return lessThan ? num <= this.val : num >= this.val;
	}

}
