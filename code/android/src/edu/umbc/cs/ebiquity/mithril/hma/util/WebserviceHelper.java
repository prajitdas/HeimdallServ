package edu.umbc.cs.ebiquity.mithril.hma.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.data.ContextData;

public class WebserviceHelper {	
	private Context context;
	private static HMADBHelper hmaDBHelper;
	private static SQLiteDatabase hmaDB;
	private static String recentlyChangedAppPackageName = new String();
	private static List<String> currentlyInstalledAppsList = new ArrayList<String>();

	/**
	 * 												Data upload portion
	 * ------------------------------------------------------------------------------------------------------------------------
	 */

	public WebserviceHelper(Context context) {
		this.context = context;
		initDB();
	}

	/**
	 * Send the data to the server
	 */
	public void sendTheData() {
		if(isOnline())
			new SendDataToServerAsyncTask().execute();// for older method HMAApplication.getConstWebserviceUri());
	}
	
//	public void sendDataSync() throws JSONException, IOException {
//		if(isOnline()) {
//			String resp;
//			String reqXMLPrefix = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:printString xmlns:ns2=\"http://webservice.hma.mithril.android.ebiquity.cs.umbc.edu/\"><arg0>";
//			String reqXMLPostfix = "</arg0></ns2:printString></S:Body></S:Envelope>";
//			
//			String request = reqXMLPrefix+writeDataToStream()+reqXMLPostfix;
//			
//			URL url;		
//			HttpURLConnection httpURLConnection = null;
//			try {
//				//Create connection
//				url = new URL(HMAApplication.getConstWebserviceUri());
//				httpURLConnection = (HttpURLConnection)url.openConnection();
//				httpURLConnection.setRequestMethod("POST");
//				httpURLConnection.setRequestProperty("Content-type", "text/xml; charset=utf-8");
//				httpURLConnection.setRequestProperty("SOAPAction", "http://eb4.cs.umbc.edu:1234/ws/datamanager#printString");
//				httpURLConnection.setChunkedStreamingMode(0);
//	
//				httpURLConnection.setUseCaches (false);
//				httpURLConnection.setDoInput(true);
//				httpURLConnection.setDoOutput(true);
//				httpURLConnection.connect();
//				
//	//				Log.d(HMAApplication.getCurrentAppsDebugTag(), "Hardcoded call starts...");
//				//Send request
//				BufferedOutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
//				out.write(request.getBytes());
//	//				Log.d(HMAApplication.getCurrentAppsDebugTag(), out.toString());
//				out.flush();
//				out.close();
//	//				Log.d(HMAApplication.getCurrentAppsDebugTag(), "Hardcoded call ends...");
//	
//				//Get Response	
//				InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
//	//				Log.d(HMAApplication.getCurrentAppsDebugTag(), "Input stream reading...");
//				resp = convertInputStreamToString(in);
//	//				Log.d(HMAApplication.getCurrentAppsDebugTag(), "Read from server: "+resp);
//			} catch (IOException e) {
//				// writing exception to log
//				e.printStackTrace();//(HMAApplication.getCurrentAppsDebugTag(), e.getStackTrace().toString());
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if(httpURLConnection != null) {
//					httpURLConnection.disconnect(); 
//				}
//			}
//		}
//	}

	private class SendDataToServerAsyncTask extends AsyncTask<String, String, String> {
		private String resp;
		// Do the long-running work in here
		@Override
	    protected String doInBackground(String... params) {
//				Log.d(HMAApplication.getCurrentAppsDebugTag(), "Loading contents...");
			publishProgress("Loading contents..."); // Calls onProgressUpdate()
			try {
//					Log.d(HMAApplication.getCurrentAppsDebugTag(), "Loading SOAP...");
				String reqXMLPrefix = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:printString xmlns:ns2=\"http://webservice.hma.mithril.android.ebiquity.cs.umbc.edu/\"><arg0>";
				String reqXMLPostfix = "</arg0></ns2:printString></S:Body></S:Envelope>";
				
				String request = reqXMLPrefix+writeDataToStream()+reqXMLPostfix;
				
				URL url;		
				HttpURLConnection httpURLConnection = null;
				try {
					//Create connection
					url = new URL(HMAApplication.getConstWebserviceUri());
					httpURLConnection = (HttpURLConnection)url.openConnection();
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setRequestProperty("Content-type", "text/xml; charset=utf-8");
					httpURLConnection.setRequestProperty("SOAPAction", "http://eb4.cs.umbc.edu:1234/ws/datamanager#printString");
					httpURLConnection.setChunkedStreamingMode(0);

					httpURLConnection.setUseCaches (false);
					httpURLConnection.setDoInput(true);
					httpURLConnection.setDoOutput(true);
					httpURLConnection.connect();
					
//						Log.d(HMAApplication.getCurrentAppsDebugTag(), "Hardcoded call starts...");
					//Send request
					BufferedOutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
					out.write(request.getBytes());
//						Log.d(HMAApplication.getCurrentAppsDebugTag(), out.toString());
					out.flush();
					out.close();
//						Log.d(HMAApplication.getCurrentAppsDebugTag(), "Hardcoded call ends...");

					//Get Response	
					InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
//						Log.d(HMAApplication.getCurrentAppsDebugTag(), "Input stream reading...");
					resp = convertInputStreamToString(in);
//						Log.d(HMAApplication.getCurrentAppsDebugTag(), "Read from server: "+resp);
				} catch (IOException e) {
					// writing exception to log
					e.printStackTrace();//(HMAApplication.getCurrentAppsDebugTag(), e.getStackTrace().toString());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} finally {
					if(httpURLConnection != null) {
						httpURLConnection.disconnect(); 
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return resp;
		}			
	    // This is called when doInBackground() is finished
	    @Override
	    protected void onPostExecute(String resp) {
	    	Toast.makeText(context, "This data was received: "+resp, Toast.LENGTH_LONG).show();
	    }

		  	/**
			 * For older method
			 * return HTTPPOST(params[0]);
			 */
	}

	private String writeDataToStream() throws JSONException, IOException {
		ContextData tempContextData = new ContextData(context);
		// Add your data
		//Create JSONObject here 
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("identity", tempContextData.getIdentity());
		jsonParam.put("modifiedApp",getRecentlyChangedAppPackageName());
//			jsonParam.put("location", appContextData.getLocation());
//			jsonParam.put("activity", appContextData.getActivity());
//			jsonParam.put("time", appContextData.getTime());
//			jsonParam.put("purpose", appContextData.getPurpose());

//		collectTheData();
		JSONArray jsonArray = new JSONArray();
		for(String applicationInfo : getCurrentlyInstalledAppsList()) {
			jsonArray.put(applicationInfo);
//			HMAApplication.addToAppList(applicationInfo);
//			        	jsonArray.put("Facebook");
//			        	jsonArray.put("Twitter");
//			        	jsonArray.put("G+");
//			Log.d(HMAApplication.getDebugTag(), "in application info writing JSON");
		}
		jsonParam.put("currentApps",jsonArray);
		jsonParam.put("deviceId",getDeviceId());
		
//		Log.d(HMAApplication.getDebugTag(), jsonParam.toString());
		return jsonParam.toString();
	}

    private String getDeviceId() {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId(); 
		tmSerial = "" + tm.getSimSerialNumber(); 
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		 
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
    }

	private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
	 * Collect running app info
	 * @return 
	 */
	public void collectTheData() {
		for(String app:hmaDBHelper.readApps(hmaDB)) {
			currentlyInstalledAppsList.add(app);
		}
	}
	
	private void initDB() {
		/**
		 * Database creation and default data insertion, happens only once.
		 */
		hmaDBHelper = new HMADBHelper(context);
		hmaDB = hmaDBHelper.getWritableDatabase();
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
//		Log.d(HMAApplication.getCurrentAppsDebugTag(), "Input stream reading complete...");
        return result;
    }
	
	public String findNewlyInstalledApp(String extraUid) {
//		Log.d(HMAApplication.getDebugTag(), "finding new app");
		Collection<String> appListPrev = new ArrayList<String>();
		appListPrev = hmaDBHelper.readApps(hmaDB);
		Collection<String> appListNow = new ArrayList<String>();
		for(ApplicationInfo appInfo : context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)) {
			try {
				if(appInfo.packageName != null) {
					appListNow.add(appInfo.packageName);
					hmaDBHelper.createApp(hmaDB, appInfo); // Add new app to database
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// Remove all elements in appListNow from appListPrev
		appListNow.removeAll(appListPrev);
		if (!appListNow.isEmpty())
			for(String appName:appListNow) {
				setRecentlyChangedAppPackageName(appName);
				return appName;
			}
		return null;
//		hmaDBHelper.createApp(hmaDB, getAppNameFromUid(extraUid));
	}

	public String findPackageChanged(String extraUid) {
		String appName = getAppNameFromUid(extraUid);
		hmaDBHelper.updateApp(hmaDB, appName);
		return appName;
	}
	
	public String findPackageRemoved(String extraUid) {
		Log.d(HMAApplication.getDebugTag(), "finding removed app");
		Collection<String> appListPrev = new ArrayList<String>();
		appListPrev = hmaDBHelper.readApps(hmaDB);
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
		appListPrev.removeAll(appListNow);
		if (!appListPrev.isEmpty())
			for(String appName:appListPrev) {
				setRecentlyChangedAppPackageName(appName);
				hmaDBHelper.deleteApp(hmaDB, appName);// Remove app from DB
				return appName;
			}
		return null;
//		hmaDBHelper.createApp(hmaDB, getAppNameFromUid(extraUid));
//		String appName = getAppNameFromUid(extraUid);
//		hmaDBHelper.deleteApp(hmaDB, appName);
//		return appName;
	}
	
	private String getAppNameFromUid(String extraUid) {
		int uid = -1;
		try {
			uid = Integer.parseInt(extraUid);
		} catch(NumberFormatException e) {
			return null;
		}
		final PackageManager pm = context.getPackageManager();
		//get a list of installed apps. 
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		String packageName = new String();
		//loop through the list of installed packages and see if the selected 
		//app is in the list 
		for (ApplicationInfo packageInfo:packages) {
			if(packageInfo.uid == uid) {
				packageName = packageInfo.packageName; //get the package info for the selected app
				break; //found a match, don't need to search anymore
			}
		}
		setRecentlyChangedAppPackageName(packageName);
		return packageName;
	}

	public String getRecentlyChangedAppPackageName() {
		return recentlyChangedAppPackageName;
	}

	public void setRecentlyChangedAppPackageName(
			String recentlyChangedAppPackageName) {
		WebserviceHelper.recentlyChangedAppPackageName = recentlyChangedAppPackageName;
	}

	public String findPackageReplaced(String extraUid) {
		String appName = getAppNameFromUid(extraUid);
		hmaDBHelper.updateApp(hmaDB, appName);
		return appName;
	}

	public List<String> getCurrentlyInstalledAppsList() {
		return currentlyInstalledAppsList;
	}

	public void setCurrentlyInstalledAppsList(
			List<String> currentlyInstalledAppsList) {
		WebserviceHelper.currentlyInstalledAppsList = currentlyInstalledAppsList;
	}
}