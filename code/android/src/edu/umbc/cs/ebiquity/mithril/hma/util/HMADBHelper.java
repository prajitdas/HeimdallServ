package edu.umbc.cs.ebiquity.mithril.hma.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;

public class HMADBHelper extends SQLiteOpenHelper {
	
	//fields for the database tables
	private final static String ID = "id";
	private final static String APPPACKAGENAME = "app_pkg_name";
	private final static String APPNAME = "app_name";
	private final static String LASTUPDATEDAT = "last_updated_at";

	// database declarations
	private final static int DATABASE_VERSION = 1;

	private final static String APP_TABLE_NAME = "appdetails";
	
	private Context context;
	
	/**
	 * Table creation statements
	 */	
	private final static String CREATE_APP_TABLE =  " CREATE TABLE " + getAppTableName() + " (" +
			getId() + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			getAppPackageName() + " TEXT UNIQUE NOT NULL DEFAULT '*', " + 
			getAppName() + " TEXT NOT NULL DEFAULT '*', " +
			getLastupdatedat() + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
	
	/**
	 * Database creation constructor
	 * @param context
	 */
	public HMADBHelper(Context context) {
		super(context, HMAApplication.getConstDatabaseName(), null, DATABASE_VERSION);
		this.setContext(context); 
	}

	public String getDatabaseName() {
		return HMAApplication.getConstDatabaseName();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public static int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	/**
	 * Table creation happens in onCreate this method also loads the default data
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_APP_TABLE);
		//The following method loads the database with the default data on creation of the database
		loadDefaultDataIntoDB(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys=ON");
	}

	/**
	 * Method to drop all tables in the database; Very dangerous
	 * @param db
	 */
	public void deleteAllData(SQLiteDatabase db) {
		dropDBObjects(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(HMADBHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ". Old data will be destroyed");
		dropDBObjects(db);
	}

	private void dropDBObjects(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " +  getAppTableName());
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 * Insert an app
	 * @param db
	 * @return
	 */
	public long createApp(SQLiteDatabase db, ApplicationInfo applicationInfo) {
		long insertedRowId;
		ContentValues values = new ContentValues();
		if(applicationInfo.loadLabel(context.getPackageManager()).toString() != null)
			values.put(getAppName(), applicationInfo.loadLabel(context.getPackageManager()).toString());
		if(applicationInfo.packageName != null)
			values.put(getAppPackageName(), applicationInfo.packageName);
		else
			return -1;
		try{
			insertedRowId = db.insert(getAppTableName(), null, values);
		} catch (SQLException e) {
            Log.e(HMAApplication.getDebugTag(), "Error inserting " + values, e);
            return -1;
		}
		return insertedRowId;
	}
	
	/**
	 * Finds all apps and return their package names
	 * @param db
	 * @return
	 */
	public List<String> readAppPackageNames(SQLiteDatabase db) {
		// Select app Query
		String selectQuery = "SELECT * FROM " + getAppTableName() + " ORDER BY " + getAppTableName() + "." + getAppName() + ";";

		List<String> apps = new ArrayList<String>();
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					apps.add(cursor.getString(1));
				} while(cursor.moveToNext());
			}
		} catch(SQLException e) {
            throw new SQLException("Could not find " + e);
		}
		return apps;
	}

	/**
	 * Finds all apps and return their package names
	 * @param db
	 * @return
	 */
	public String readAppNameByPackageName(SQLiteDatabase db, String appPackageName) {
		// Select app Query
		String selectQuery = "SELECT " + getAppTableName() + "." + getAppName() + " FROM " + getAppTableName() + 
				" WHERE " + getAppTableName() + "." + getAppPackageName() + " = '" + appPackageName +"';";
		String app = new String();
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					app = cursor.getString(0);
				} while(cursor.moveToNext());
			}
		} catch(SQLException e) {
            throw new SQLException("Could not find " + e);
		}
		return app;
	}

	/**
	 * method to update single app
	 * @param db
	 * @param appPkgName
	 */
	public int updateApp(SQLiteDatabase db, String appPkgName) {
		ContentValues values = new ContentValues();
		values.put(getLastupdatedat(), getDateTime());
		return db.update(getAppTableName(), values, getAppPackageName() + " = ?", 
				new String[] { appPkgName });
	}
	
	/**
	 * method to delete an app from a table based on the appPkgName 
	 * @param db
	 * @param appPkgName
	 */
	public void deleteApp(SQLiteDatabase db, String appPkgName) {
		try {
			db.delete(getAppTableName(), getAppPackageName() + " = ?",
					new String[] { appPkgName });
		} catch(SQLException e) {
			throw new SQLException("Could not find " + e);
		} 
	}
	
	/**
	 * Table name getters
	 * @return the table name
	 */
	public static String getAppTableName() {
		return APP_TABLE_NAME;
	}

	
	/**
	 * method to load the default set of policies into the database
	 * @param db reference to the db instance
	 */
	private void loadDefaultDataIntoDB(SQLiteDatabase db) {
		//get all apps
		for(ApplicationInfo applicationInfo:context.getPackageManager().getInstalledApplications(
				PackageManager.GET_META_DATA 
				| PackageManager.GET_SHARED_LIBRARY_FILES 
				| PackageManager.GET_UNINSTALLED_PACKAGES))
			createApp(db, applicationInfo);
	}
	public static String getId() {
		return ID;
	}

	public static String getAppPackageName() {
		return APPPACKAGENAME;
	}

	public static String getAppName() {
		return APPNAME;
	}

	public static String getLastupdatedat() {
		return LASTUPDATEDAT;
	}
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
}