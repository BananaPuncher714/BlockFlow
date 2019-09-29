package io.github.bananapuncher714.blockflow.api.db;

import java.util.HashMap;
import java.util.Map;

public class DBEntry {
	protected Map< Category, String > objects = new HashMap< Category, String >();
	
	public DBEntry() {
	}
	
	public void set( Category category, String object ) {
		objects.put( category, object );
	}
	
	public boolean contains( Category category ) {
		return objects.containsKey( category );
	}
	
	public String get( Category category ) {
		return objects.get( category );
	}
	
	public int getInt( Category category ) {
		if ( objects.containsKey( category ) ) {
			return Integer.parseInt( objects.get( category ) );
		}
		return 0;
	}
	
	public double getDouble( Category category ) {
		if ( objects.containsKey( category ) ) {
			return Double.parseDouble( objects.get( category ) );
		}
		return 0;
	}
}
