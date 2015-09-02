package edu.umbc.cs.ebiquity.mithril.hma.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.util.WebserviceHelper;

public class CurrentAppsService extends IntentService {
	private static WebserviceHelper webserviceHelper;
	
	public CurrentAppsService() {
		super("CurrentAppsService");
	}		

	@Override
	protected void onHandleIntent(Intent intent) {
		webserviceHelper = new WebserviceHelper(getApplicationContext());
		webserviceHelper.collectTheData();
		webserviceHelper.sendTheData();
        
        /* Task complete now return */
        Intent intentOnCompletionOfDataCollection = new Intent(HMAApplication.getConstDataCollectionComplete());
//        intentOnCompletionOfDataCollection.putExtra(CONST_LIST_OF_RUNNING_APPS_EXTRA, output.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOnCompletionOfDataCollection);
	}
}