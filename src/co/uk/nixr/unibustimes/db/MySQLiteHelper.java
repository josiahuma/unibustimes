package co.uk.nixr.unibustimes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_BUS_STOP = "busstops";
	public static final String COLUMN_BUS_STOP_ID = "_id";
	public static final String COLUMN_BUS_STOP_NAME = "";

	public static final String TABLE_BUS_STOP_TIMES = "bustimes";
	public static final String COLUMN_TIME = "00:00";
	public static final String COLUMN_BUS_ID = "_id";
	public static final String COLUMN_DESTINATION = "destination";


	private static final String DATABASE_NAME = "bustimes.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BUS_STOP + "( " + COLUMN_BUS_STOP_ID 
			+ " integer primary key autoincrement, " +  COLUMN_BUS_STOP_NAME
			+ " text not null);" + 
			"create table "
			+ TABLE_BUS_STOP_TIMES + "( " + COLUMN_TIME 
			+ " text primary key not null, " +  COLUMN_BUS_ID
			+ " text not null," +  COLUMN_DESTINATION
			+ "text not null" +
			"FOREIGN KEY ("+COLUMN_BUS_ID+") REFERENCES "+TABLE_BUS_STOP+" ("+COLUMN_BUS_STOP_ID+"));";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_BUS_STOP);
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_BUS_STOP_TIMES);
		onCreate(db);
	}

}

