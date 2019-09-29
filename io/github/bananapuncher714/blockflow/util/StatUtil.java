package io.github.bananapuncher714.blockflow.util;

import java.util.List;

public class StatUtil {
	public static double mean( List< Double > data ) {
		double sum = 0;
		for ( double num : data ) {
			sum += num;
		}
		return sum / ( double ) data.size();
	}
	
	public static double standardDev( List< Double > data ) {
		double mean = mean( data );
		
		double totalDev = 0;
		for ( double num : data ) {
			double diff = ( mean - num );
			totalDev += diff * diff;
		}
		double dev = totalDev / ( double ) data.size();
		return Math.sqrt( dev );
	}
	
	public static double max( List< Double > data ) {
		double highest = 0;
		for ( double val : data ) {
			highest = Math.max( val, highest );
		}
		return highest;
	}
	
	public static double min( List< Double > data ) {
		double lowest = Double.MAX_VALUE;
		for ( double val : data ) {
			lowest = Math.min( val, lowest );
		}
		return lowest;
	}
}
