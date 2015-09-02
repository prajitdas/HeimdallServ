package edu.umbc.cs.ebiquity.mithril.hma.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.service.CurrentAppsService;
import edu.umbc.cs.ebiquity.mithril.hma.util.AppsAdapter;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class ShowAppsMainActivity extends ListActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	private Intent mServiceIntent;
	
	/** 
     * Provides the entry point to Google Play services. 
     */ 
    protected GoogleApiClient mGoogleApiClient;
	private PackageManager packageManager;
	private List<ApplicationInfo> appsList;
	private AppsAdapter listAdapter;
//	private TextView mCurrentAppsDataCollectionAgreementTxtView;
//	private Button mAcceptAgreementBtn;
//	private Button mStartSvcBtn;
	
	private static HMADBHelper hmaDBHelper;
	private static SQLiteDatabase hmaDB;

	/** 
     * Represents a geographical location. 
     */ 
    protected Location mLastLocation;
 
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/**
		 * If you want to load the apps this is what you do
		new LoadApps().execute();
		 */
		initDB();
		initViews();
//		setOnClickListeners();
//		storeListOfAppsInstalled();
		StringBuffer result = new StringBuffer();
		for(String app:hmaDBHelper.readApps(hmaDB)) {
			result.append(app);
		}
		
		Log.d(HMAApplication.getDebugTag(), "List has: "+result.toString());
		startCurrentAppsService();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter currentAppsServiceIntentFilter = new IntentFilter(HMAApplication.getConstDataCollectionComplete());
		LocalBroadcastManager.getInstance(this).registerReceiver(onEvent, currentAppsServiceIntentFilter);
	}
	@Override
		public void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(onEvent);
		super.onPause();
	}
	
	private BroadcastReceiver onEvent = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent i) {
	//		b.setEnabled(true);
			Toast.makeText(getApplicationContext(), "Data collection complete", Toast.LENGTH_LONG).show();
		}
	};

	private void initDB() {
		/**
		 * Database creation and default data insertion, happens only once.
		 */
		hmaDBHelper = new HMADBHelper(this);
		hmaDB = hmaDBHelper.getWritableDatabase();
	}
	
	private void initViews() {
		buildGoogleApiClient();
//		File file = new File(getFilesDir(), HMAApplication.getApplistFilename());
		HMAApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));

		packageManager = getApplicationContext().getPackageManager();
		appsList = new ArrayList<ApplicationInfo>();

		List<ApplicationInfo> tempAppsList = new ArrayList<ApplicationInfo>();
		tempAppsList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		for(ApplicationInfo appInfo : tempAppsList) {
			try {
				if(appInfo.packageName != null) {
					appsList.add(appInfo);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		listAdapter = new AppsAdapter(ShowAppsMainActivity.this, R.layout.app_list_item, appsList);
		setListAdapter(listAdapter);

/*
		mCurrentAppsDataCollectionAgreementTxtView = (TextView) findViewById(R.id.currentAppsDataCollectionAgreementTxtView);
		mCurrentAppsDataCollectionAgreementTxtView.setText(R.string.agreementText);

		mAcceptAgreementBtn = (Button) findViewById(R.id.acceptAgreementBtn);		
		mStartSvcBtn = (Button) findViewById(R.id.startCurrentAppsSvcBtn);
		
		boolean acceptedOrNot;
		if(HMAApplication.getPreferences().contains(HMAApplication.getConstAcceptDecisionKey())) {
			acceptedOrNot = HMAApplication.getPreferences().getBoolean(HMAApplication.getConstAcceptDecisionKey(), false);
			if(acceptedOrNot) {
				mAcceptAgreementBtn.setEnabled(false);
				mStartSvcBtn.setEnabled(true);
			} else {
				mAcceptAgreementBtn.setEnabled(true);
				mStartSvcBtn.setEnabled(false);
			}
		}
		else {
			mAcceptAgreementBtn.setEnabled(true);
			mStartSvcBtn.setEnabled(false);
		}*/
		HMAApplication.setContextData(this, mLastLocation);
	}

	/** 
	 * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API. 
	 */ 
	protected synchronized void buildGoogleApiClient() { 
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	            .addConnectionCallbacks(this)
	            .addOnConnectionFailedListener(this)
	            .addApi(LocationServices.API) 
	            .build(); 
	} 
	 
 
    @Override 
    protected void onStart() { 
        super.onStart(); 
        mGoogleApiClient.connect();
    } 
 
    @Override 
    protected void onStop() { 
        super.onStop(); 
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        } 
    } 
    
//    private void storeListOfAppsInstalled() {
//		AppListJson appListJson = new AppListJson();
//		for(ApplicationInfo appInfo : getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)) {
//			try {
//				if(appInfo.packageName != null)
//					appListJson.appList.add(appInfo.packageName);
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//		Gson gson = new Gson();
//		String jsonData = gson.toJson(appListJson);
//		SharedPreferences.Editor appListEditor = HMAApplication.getPreferences().edit();
//		appListEditor.putString("appListJson", jsonData);
//		appListEditor.commit();
//		writeToFile(jsonData);
//	}
//
//	private void writeToFile(String data) {
//		FileOutputStream outputStream;
//	    try { 
//	    	outputStream = openFileOutput(HMAApplication.getApplistFilename(), Context.MODE_PRIVATE);
//	    	outputStream.write(data.getBytes());
//	    	outputStream.close();
//	    } 
//	    catch (FileNotFoundException e) {
//	        Log.e("Exception", "File write failed: " + e.toString());
//	    }  
//	    catch (IOException e) {
//	        Log.e("Exception", "File write failed: " + e.toString());
//	    }  
//	} 
	
/*	private void setOnClickListeners() {
		mAcceptAgreementBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = HMAApplication.getPreferences().edit();
		        editor.putBoolean(HMAApplication.getConstAcceptDecisionKey(), true);
		        editor.commit();

		        mStartSvcBtn.setEnabled(true);
				mAcceptAgreementBtn.setEnabled(false);//.setVisibility(View.GONE);
			}
		});

		mStartSvcBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startCurrentAppsService();
			}
		});
	}
*/
	private void startCurrentAppsService() {
		/*
		 * Creates a new Intent to start the RSSPullService
		 * IntentService. Passes a URI in the
		 * Intent's "data" field.
		 */
		mServiceIntent = new Intent(this, CurrentAppsService.class);
//		mServiceIntent.setData();
		
		// Starts the IntentService
		this.startService(mServiceIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** 
     * Runs when a GoogleApiClient object successfully connects. 
     */ 
	@Override 
	public void onConnected(Bundle connectionHint) {
	    // Provides a simple way of getting a device's location and is well suited for 
		// applications that do not require a fine-grained location and that do not need location 
		// updates. Gets the best and most recent location currently available, which may be null 
		// in rare cases when a location is not available. 
	    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
	    if (mLastLocation != null) {
	        mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
	        mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
	    } else { 
	        Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
	    } 
	}

    @Override 
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in 
		// onConnectionFailed. 
		Log.i(HMAApplication.getDebugTag(), "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	} 

	@Override 
	public void onConnectionSuspended(int cause) {
	    // The connection to Google Play services was lost for some reason. We call connect() to 
		// attempt to re-establish the connection. 
		Log.i(HMAApplication.getDebugTag(), "Connection suspended");
		mGoogleApiClient.connect();
	}
}