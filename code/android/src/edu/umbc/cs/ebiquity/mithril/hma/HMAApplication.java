package edu.umbc.cs.ebiquity.mithril.hma;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

public class HMAApplication extends Application {
	private static SharedPreferences preferences;
	private static final String APPLIST_FILENAME = "applist.json";
	private static final String DEBUG_TAG = "CURRENT_APPS_DEBUG_TAG";
	private static final String CONST_DATA_COLLECTION_COMPLETE = "CONST_DATA_COLLECTION_COMPLETE";
	private static final String CONST_ACCEPT_DECISION_KEY = "acceptDecisionKey";	
	private static final String CONST_WEBSERVICE_URI = "http://eb4.cs.umbc.edu:1234/ws/datamanager";
	private static final String CONST_NOTIFICATION_TITLE = "HMAApplication notification";
	private static List<String> appList = new ArrayList<String>();
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
	public static List<String> getAppList() {
		return appList;
	}
	public static String getAppListString() {
		StringBuffer result = new StringBuffer();
		for(String app:appList) {
			result.append(app);
		}
		return result.toString();
	}
	public static void setAppList(List<String> appList) {
		HMAApplication.appList = appList;
	}
	public static void addToAppList(String appName) {
		if(!appList.contains(appName))
			appList.add(appName);
	}
	public static boolean isInAppList(ApplicationInfo ApplicationInfo) {
		if(appList.contains(ApplicationInfo.packageName))
			return true;
		return false;
	}
	public static String getDebugTag() {
		return DEBUG_TAG;
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