package edu.umbc.cs.ebiquity.mithril.hma.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.ui.NotificationView;
import edu.umbc.cs.ebiquity.mithril.hma.util.WebserviceHelper;

public class AppInstallBroadcastReceiver extends BroadcastReceiver {
	private static WebserviceHelper webserviceHelper;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		webserviceHelper = new WebserviceHelper(context);
		String message = new String();
		/**
		 * Broadcast Action: A new application package has been installed on the device. The data contains the name of the package. Note that the newly installed package does not receive this broadcast.
		 * May include the following extras:
		 * EXTRA_UID containing the integer uid assigned to the new package.
		 * EXTRA_REPLACING is set to true if this is following an ACTION_PACKAGE_REMOVED broadcast for the same package.
		 * This is a protected intent that can only be sent by the system.
		 * Constant Value: "android.intent.action.PACKAGE_ADDED"
		 */
		if(intent.getAction() == "android.intent.action.PACKAGE_ADDED") {
			Log.d(HMAApplication.getDebugTag(), "intent received");
			message = "New app installed is: " + webserviceHelper.findNewlyInstalledApp(Intent.EXTRA_UID);
			webserviceHelper.collectTheData();
			webserviceHelper.sendTheData();
//			try {
//				webserviceHelper.sendDataSync();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		/**
		 * Broadcast Action: An existing application package has been changed (e.g. a component has been enabled or disabled). The data contains the name of the package.
		 * EXTRA_UID containing the integer uid assigned to the package.
		 * EXTRA_CHANGED_COMPONENT_NAME_LIST containing the class name of the changed components (or the package name itself).
		 * EXTRA_DONT_KILL_APP containing boolean field to override the default action of restarting the application.
		 * This is a protected intent that can only be sent by the system.
		 * Constant Value: "android.intent.action.PACKAGE_CHANGED"
		 */
		else if(intent.getAction() == "android.intent.action.PACKAGE_CHANGED") {
			/**
			 * Don't send data on update for now
			 */
			Log.d(HMAApplication.getDebugTag(), "package changed, nothing to do");
//			message = "An existing application package has been changed (e.g. a component has been enabled or disabled): " + webserviceHelper.findPackageChanged(Intent.EXTRA_UID);
//			webserviceHelper.collectTheData();
//			webserviceHelper.sendTheData();
		}
		/**
		 * Broadcast Action: An existing application package has been removed from the device. The data contains the name of the package. The package that is being installed does not receive this Intent.
		 * EXTRA_UID containing the integer uid previously assigned to the package.
		 * EXTRA_DATA_REMOVED is set to true if the entire application -- data and code -- is being removed.
		 * EXTRA_REPLACING is set to true if this will be followed by an ACTION_PACKAGE_ADDED broadcast for the same package.
		 * This is a protected intent that can only be sent by the system.
		 * Constant Value: "android.intent.action.PACKAGE_REMOVED"
		 */
		else if(intent.getAction() == "android.intent.action.PACKAGE_REMOVED") {
			/**
			 * Don't send data on uninstall app for now
			 */
			Log.d(HMAApplication.getDebugTag(), "package removed, nothing to do");
//			message = "An existing application package has been removed from the device: " + webserviceHelper.findPackageRemoved(Intent.EXTRA_UID);
//			webserviceHelper.collectTheData();
//			webserviceHelper.sendTheData();
		}
		/**
		 * Broadcast Action: A new version of an application package has been installed, replacing an existing version that was previously installed. The data contains the name of the package.
		 * May include the following extras:
		 * EXTRA_UID containing the integer uid assigned to the new package.
		 * This is a protected intent that can only be sent by the system.
		 * Constant Value: "android.intent.action.PACKAGE_REPLACED"
		 */
		else if(intent.getAction() == "android.intent.action.PACKAGE_REPLACED") {
			/**
			 * Don't send data on update for now
			 */
			Log.d(HMAApplication.getDebugTag(), "package replaced, nothing to do");
//			message = "New app installed is: " + webserviceHelper.findPackageReplaced(Intent.EXTRA_UID);
//			webserviceHelper.collectTheData();
//			webserviceHelper.sendTheData();
		}
		Notification(context, message);
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
//				.addAction(R.drawable.logosmall, "Action Button", pIntent)
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