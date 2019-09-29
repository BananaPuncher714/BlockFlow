package io.github.bananapuncher714.blockflow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.blockflow.api.canvas.OverlaySubcanvas;
import io.github.bananapuncher714.blockflow.api.canvas.WorldSubcanvas;
import io.github.bananapuncher714.blockflow.api.db.Category;
import io.github.bananapuncher714.blockflow.api.db.FlatFileDB;
import io.github.bananapuncher714.blockflow.api.poly.FlatPoly;
import io.github.bananapuncher714.blockflow.file.CSVReader;
import io.github.bananapuncher714.blockflow.file.PolyUtil;
import io.github.bananapuncher714.blockflow.util.FileUtil;
import io.github.bananapuncher714.blockflow.util.StatUtil;

public class BlockFlow extends JavaPlugin {
	private Canvas mainCanvas;
	private FlatFileDB db;
	
	private BlockFlowCommand command;
	
	private Map< String, FlatPoly > polys;
	
	private File CSV_FILE;
	private File POLY_FILE;
	
	private WorldSubcanvas worldSubcanvas;
	private OverlaySubcanvas overlaySubcanvas;
	
	private double popDev;
	private double popMean;
	private double popMax;
	private double popMin;
	
	private double mHeightDev;
	private double mHeightMean;
	
	private double fHeightDev;
	private double fHeightMean;
	
	@Override
	public void onEnable() {
		CSV_FILE = new File( getDataFolder() + "/" + "uberdb.csv" );
		POLY_FILE = new File( getDataFolder() + "/" + "poly.dat" );
		
		init();
		
		load();
		
		command = new BlockFlowCommand( this );
		
		getCommand( "blockflow" ).setExecutor( command );
		getCommand( "blockflow" ).setTabCompleter( command );
		
		List< Double > popVals = new ArrayList< Double >();
		for ( String str : db.getValuesFor( new Category( "Population" ) ) ) {
			if ( !str.isEmpty() ) {
				popVals.add( Double.parseDouble( str ) );
			}
		}
		
		popDev = StatUtil.standardDev( popVals );
		popMean = StatUtil.mean( popVals );
		popMax = StatUtil.max( popVals );
		popMin = StatUtil.min( popVals );
		
		List< Double > fHeights = new ArrayList< Double >();
		for ( String str : db.getValuesFor( new Category( "Mean women height(cm)" ) ) ) {
			if ( !str.isEmpty() ) {
				fHeights.add( Double.parseDouble( str ) );
			}
		}
		
		fHeightDev = StatUtil.standardDev( fHeights );
		fHeightMean = StatUtil.mean( fHeights );
		
		List< Double > mHeights = new ArrayList< Double >();
		for ( String str : db.getValuesFor( new Category( "Mean men height(cm)" ) ) ) {
			if ( !str.isEmpty() ) {
				mHeights.add( Double.parseDouble( str ) );
			}
		}
		
		mHeightDev = StatUtil.standardDev( mHeights );
		mHeightMean = StatUtil.mean( mHeights );
		
		System.out.println( "Statistics(stdev/mean):" );
		System.out.println( "Population: " + popDev + " " + popMean );
		System.out.println( "Men Height: " + mHeightDev + " " + mHeightMean );
		System.out.println( "Women Height: " + fHeightDev + " " + fHeightMean );
	}
	
	@Override
	public void onDisable() {
		if ( mainCanvas != null ) {
			mainCanvas.disable();
		}
	}
	
	private void init() {
		FileUtil.saveToFile( getResource( "data/uberdb.csv" ), CSV_FILE, false );
		FileUtil.saveToFile( getResource( "data/poly.dat" ), POLY_FILE, false );
	}

	private void load() {
		 db = CSVReader.read( CSV_FILE );
		 getLogger().info( "Uber DB Loaded!" );
		 polys = PolyUtil.load( POLY_FILE );
		 getLogger().info( "Polys loaded!" );
	}
	
	public Canvas getMainCanvas() {
		return mainCanvas;
	}

	public void setMainCanvas( Canvas mainCanvas ) {
		if ( mainCanvas != null  ) {
			mainCanvas.disable();
		}
		this.mainCanvas = mainCanvas;
	}
	
	public WorldSubcanvas getWorldSubcanvas() {
		return worldSubcanvas;
	}

	public void setWorldSubcanvas( WorldSubcanvas worldSubcanvas ) {
		this.worldSubcanvas = worldSubcanvas;
	}

	public OverlaySubcanvas getOverlaySubcanvas() {
		return overlaySubcanvas;
	}

	public void setOverlaySubcanvas( OverlaySubcanvas overlaySubcanvas ) {
		this.overlaySubcanvas = overlaySubcanvas;
	}

	public FlatFileDB getDb() {
		return db;
	}

	public Map< String, FlatPoly > getPolys() {
		return polys;
	}

	public double getPopDev() {
		return popDev;
	}

	public double getPopMean() {
		return popMean;
	}

	public double getmHeightDev() {
		return mHeightDev;
	}

	public double getmHeightMean() {
		return mHeightMean;
	}

	public double getfHeightDev() {
		return fHeightDev;
	}

	public double getfHeightMean() {
		return fHeightMean;
	}

	public double getPopMax() {
		return popMax;
	}

	public double getPopMin() {
		return popMin;
	}
}
