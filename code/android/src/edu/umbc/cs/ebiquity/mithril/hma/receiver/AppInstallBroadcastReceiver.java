package edu.umbc.cs.ebiquity.mithril.hma.receiver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.ui.NotificationView;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class AppInstallBroadcastReceiver extends BroadcastReceiver {
	private static HMADBHelper hmaDBHelper;
	private static SQLiteDatabase hmaDB;

	@Override
	public void onReceive(Context context, Intent intent) {
		initDB(context);
		Log.d(HMAApplication.getDebugTag(), "intent received");
		String message = new String();
		message = findNewlyInstalledApp(context);
		Log.d(HMAApplication.getDebugTag(), "message received, it says: "+message);
		if(intent.getAction() == "android.intent.action.PACKAGE_ADDED")
			message = "New app installed: " + message;
//		else if(intent.getAction() == "android.intent.action.PACKAGE_CHANGED")
//			message = "Component changed (enabled or disabled): " + message;
//		else if(intent.getAction() == "android.intent.action.PACKAGE_INSTALL")
//			message = "Trigger the download and eventual installation of package: " + message;
//		else if(intent.getAction() == "android.intent.action.PACKAGE_REMOVED")
//			message = "Existing application package: "+message+" has been removed from the device";
//		else if(intent.getAction() == "android.intent.action.PACKAGE_REPLACED")
//			message = "A new version of application package: "+message+"has been installed";
		Notification(context, message);
	}

	private void initDB(Context context) {
		/**
		 * Database creation and default data insertion, happens only once.
		 */
		hmaDBHelper = new HMADBHelper(context);
		hmaDB = hmaDBHelper.getWritableDatabase();
	}
	
	private String findNewlyInstalledApp(Context context) {
		Log.d(HMAApplication.getDebugTag(), "processing");
		Collection<String> appListPrev = new ArrayList<String>();
		appListPrev = hmaDBHelper.readApps(hmaDB);
//		try {
//			//JSONObject jObject = new JSONObject(HMAApplication.getPreferences().getString("appListJson", ""));
//			JSONObject jObject = new JSONObject(readFromFile(context));
//			JSONArray jArray = jObject.getJSONArray("appList");
//			for(int i=0; i < jArray.length(); i++) { 
//			    try { 
//			    	appListPrev.add(jArray.getString(i));
//			    } catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//			    }
//			} 
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		Log.d(HMAApplication.getDebugTag(), "List has: "+appListPrev);
		Collection<String> appListNow = new ArrayList<String>();
		for(ApplicationInfo appInfo : context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)) {
			try {
				if(appInfo.packageName != null) {
					appListNow.add(appInfo.packageName);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// Remove all elements in appListNow from appListPrev
		appListNow.removeAll(appListPrev);
		if (!appListNow.isEmpty())
			for(String newAppInstalledName:appListNow)
				return newAppInstalledName;
		return null;
	}

	private String readFromFile(Context context) {
		 
	    String ret = "";
	 
	    try { 
	        InputStream inputStream = context.getApplicationContext().openFileInput(HMAApplication.getApplistFilename());
	 
	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();
	 
	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            } 
	 
	            inputStream.close();
	            ret = stringBuilder.toString();
	        } 
	    } 
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    } 
	 
	    return ret;
	} 

	public void Notification(Context context, String message) {
		// Open NotificationView Class on Notification Click
		Intent intent = new Intent(context, NotificationView.class);
		// Send data to NotificationView Class
		intent.putExtra("title", message);
		// Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
 
		// Create Notification using NotificationCompat.Builder
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)
				// Set Icon
				.setSmallIcon(R.drawable.logosmall)
				// Set Ticker Message
				.setTicker(message)
				// Set Title
				.setContentTitle(context.getString(R.string.notificationtitle))
				// Set Text
				.setContentText(message)
				// Add an Action Button below Notification
				.addAction(R.drawable.ic_launcher, "Action Button", pIntent)
				// Set PendingIntent into Notification
				.setContentIntent(pIntent)
				// Dismiss Notification
				.setAutoCancel(true);
 
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());
 
	}
}