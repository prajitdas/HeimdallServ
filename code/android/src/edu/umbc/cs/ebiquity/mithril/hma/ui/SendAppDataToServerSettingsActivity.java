package edu.umbc.cs.ebiquity.mithril.hma.ui;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.service.CurrentAppsService;

public class SendAppDataToServerSettingsActivity extends PreferenceActivity {
	private Intent mServiceIntent;

	@SuppressWarnings("deprecation")
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_activity_send_app_data_to_server);
		startCurrentAppsService();
		HMAApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));
	} 

	private void startCurrentAppsService() {
		/**
		 * If service is running don't restart it 
		 * Otherwise create a new Intent to start the service
		 */
		if(!isServiceRunning(CurrentAppsService.class)) {
			mServiceIntent = new Intent(this, CurrentAppsService.class);
			this.startService(mServiceIntent);
		}
	}

    /**
     * Code from: http://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
     * @param serviceClass
     * @return true if service is running or else false
     */
	private boolean isServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
			if (serviceClass.getName().equals(service.service.getClassName()))
				return true; 
		return false; 
	} 

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
//	HMAApplication.setContextData(this, mLastLocation);
//}

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
}