package edu.umbc.cs.ebiquity.mithril.hma.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.data.AppListJson;
import edu.umbc.cs.ebiquity.mithril.hma.service.CurrentAppsService;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class ShowAppsMainActivity extends Activity {
	private Intent mServiceIntent;
	
//	private TextView mTextViewPermissionInfo;
//	private PackageManager packageManager;
//	private List<ApplicationInfo> appsList;
//	private AppsAdapter listAdapter;
	private TextView mCurrentAppsDataCollectionAgreementTxtView;
	private Button mAcceptAgreementBtn;
	private Button mStartSvcBtn;
	
	private static HMADBHelper hmaDBHelper;
	private static SQLiteDatabase hmaDB;

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
		setOnClickListeners();
		storeListOfAppsInstalled();
		StringBuffer result = new StringBuffer();
		for(String app:hmaDBHelper.readApps(hmaDB)) {
			result.append(app);
		}
		
		Log.d(HMAApplication.getDebugTag(), "List has: "+result.toString());
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
//		File file = new File(getFilesDir(), HMAApplication.getApplistFilename());
		HMAApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));

//		packageManager = getApplicationContext().getPackageManager();
//		appsList = new ArrayList<ApplicationInfo>();

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
		}
	}

	private void storeListOfAppsInstalled() {
		AppListJson appListJson = new AppListJson();
		for(ApplicationInfo appInfo : getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)) {
			try {
				if(appInfo.packageName != null)
					appListJson.appList.add(appInfo.packageName);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(appListJson);
		SharedPreferences.Editor appListEditor = HMAApplication.getPreferences().edit();
		appListEditor.putString("appListJson", jsonData);
		appListEditor.commit();
		writeToFile(jsonData);
	}

	private void writeToFile(String data) {
		FileOutputStream outputStream;
	    try { 
	    	outputStream = openFileOutput(HMAApplication.getApplistFilename(), Context.MODE_PRIVATE);
	    	outputStream.write(data.getBytes());
	    	outputStream.close();
	    } 
	    catch (FileNotFoundException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    }  
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    }  
	} 
	
	private void setOnClickListeners() {
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
}