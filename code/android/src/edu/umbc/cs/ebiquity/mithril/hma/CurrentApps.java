package edu.umbc.cs.ebiquity.mithril.hma;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CurrentApps extends Application {
	private static final String CURRENT_APPS_DEBUG_TAG = "CURRENT_APPS_DEBUG_TAG";
	private static final String CONST_DATA_COLLECTION_COMPLETE = "CONST_DATA_COLLECTION_COMPLETE";
	private static final String CONST_ACCEPT_DECISION_KEY = "acceptDecisionKey";	
	private static final String CONST_WEBSERVICE_URI = "http://eb4.cs.umbc.edu:1234/ws/datamanager";
	private static final String CONST_NOTIFICATION_TITLE = "CurrentApps notification";
	
	public static String getCurrentAppsDebugTag() {
		return CURRENT_APPS_DEBUG_TAG;
	}
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
}