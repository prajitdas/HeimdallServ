package edu.umbc.cs.ebiquity.mithril.hma;

import android.app.Application;
import android.content.SharedPreferences;

public class HMAApplication extends Application {
	private static SharedPreferences preferences;
	private static final String APPLIST_FILENAME = "applist.json";
	private static final String CONST_DEBUG_TAG = "HMA_APPLICATION_DEBUG_TAG";
	private static final String CONST_DATA_COLLECTION_COMPLETE = "CONST_DATA_COLLECTION_COMPLETE";
	private static final String CONST_ACCEPT_DECISION_KEY = "acceptDecisionKey";	
	private static final String CONST_WEBSERVICE_URI = "http://eb4.cs.umbc.edu:1234/ws/datamanager";
	private static final String CONST_NOTIFICATION_TITLE = "HMAApplication notification";
	private final static String CONST_DATABASE_NAME = "HMADB";
	
	public static String getConstDataCollectionComplete() {
		return CONST_DATA_COLLECTION_COMPLETE;
	}
	public static String getConstAcceptDecisionKey() {
		return CONST_ACCEPT_DECISION_KEY;
	}
	public static String getConstNotificationTitle() {
		return CONST_NOTIFICATION_TITLE;
	}
	public static String getConstWebserviceUri() {
		return CONST_WEBSERVICE_URI;
	}
	public static String getDebugTag() {
		return CONST_DEBUG_TAG;
	}
	public static String getApplistFilename() {
		return APPLIST_FILENAME;
	}
	public static SharedPreferences getPreferences() {
		return preferences;
	}
	public static void setPreferences(SharedPreferences preferences) {
		HMAApplication.preferences = preferences;
	}
	public static String getConstDatabaseName() {
		return CONST_DATABASE_NAME;
	}
}