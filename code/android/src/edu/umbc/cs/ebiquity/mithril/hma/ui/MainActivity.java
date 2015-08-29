package edu.umbc.cs.ebiquity.mithril.hma.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.service.CurrentAppsService;

public class MainActivity extends Activity {
	private Intent mServiceIntent;
	
	private TextView mCurrentAppsDataCollectionAgreementTxtView;
	private Button mAcceptAgreementBtn;
	private Button mStartSvcBtn;
	
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		setOnClickListeners();
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

	private void init() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		mCurrentAppsDataCollectionAgreementTxtView = (TextView) findViewById(R.id.currentAppsDataCollectionAgreementTxtView);
		mCurrentAppsDataCollectionAgreementTxtView.setText(R.string.agreementText);

		mAcceptAgreementBtn = (Button) findViewById(R.id.acceptAgreementBtn);		
		mStartSvcBtn = (Button) findViewById(R.id.startCurrentAppsSvcBtn);
		
		boolean acceptedOrNot;
		if(preferences.contains(HMAApplication.getConstAcceptDecisionKey())) {
			acceptedOrNot = preferences.getBoolean(HMAApplication.getConstAcceptDecisionKey(), false);
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

	private void setOnClickListeners() {
		mAcceptAgreementBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = preferences.edit();
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