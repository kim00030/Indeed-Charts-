package com.ilhyungkim.indeedcharts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChartsDBOpenHelper extends SQLiteOpenHelper {
	
	//private static final String LOGTAG = "INDEED_CHART";
	private static final String DATABASE_NAME = "charts.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_CHARTS = "charts";
	/**Primary key*/
	public static final String COLUMN_MAIN_ID = "chartMainId";
	/**Id for type of chart*/
	public static final String COLUMN_CHART_ID = "chartId";
	
	public static final String COLUMN_TITLE = "title";
	
	public static final String COLUMN_DATE = "date";
	/** chart's image file name*/
	public static final String COLUMN_IMAGE_NAME = "imageName";
	
	public static final String COLUMN_SREIES_DATA = "seriesData";
	
	private static final String TABLE_CREATE = 
			"CREATE TABLE "+TABLE_CHARTS + " ("+
			COLUMN_MAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			COLUMN_CHART_ID + " INTEGER, "+
			COLUMN_TITLE + " TEXT, "+
			COLUMN_DATE + " TEXT, "+
			COLUMN_IMAGE_NAME+ " TEXT, "+
			COLUMN_SREIES_DATA+ " TEXT "+
			")";
	
	public ChartsDBOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHARTS);
		onCreate(db);
	}

}
