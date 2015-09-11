package edu.umbc.cs.ebiquity.mithril.hma.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.util.AppListAdapter;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class ShowAppsMainActivity extends ListActivity {
	private PackageManager packageManager;
	private List<ApplicationInfo> appList;
	
	private static HMADBHelper hmaDBHelper;
	private static SQLiteDatabase hmaDB;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * ListActivity has a default layout that consists of a single, full-screen list in the center of the screen. 
		 * However, if you desire, you can customize the screen layout by setting your own view layout with setContentView() in onCreate(). 
		 * To do this, your own view MUST contain a ListView object with the id "@android:id/list" (or list if it's in code)
		 */
		setContentView(R.layout.activity_show_apps_main);
		
		initDB();
		initView();
	}
	
	private void initDB() {
		/**
		 * Database creation and default data insertion, happens only once.
		 */
		hmaDBHelper = new HMADBHelper(this);
		hmaDB = hmaDBHelper.getWritableDatabase();
	}
	
	private void initView() {
		packageManager = getApplicationContext().getPackageManager();
		setAppList();
		// Bind to new adapter. 
		setListAdapter(new AppListAdapter(ShowAppsMainActivity.this, R.layout.app_list_item, getAppList()));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ApplicationInfo applicationInfo = (ApplicationInfo) getListAdapter().getItem(position);
		Intent viewAppDetailsActivityIntent = new Intent(this, ViewAppDetailsActivity.class);
		viewAppDetailsActivityIntent.putExtra(HMADBHelper.getAppPackageName(), applicationInfo.packageName);
		startActivityForResult(viewAppDetailsActivityIntent, 0);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.send_data_to_server_settings) {
			startActivityForResult(new Intent(this, SendAppDataToServerSettingsActivity.class), 0); 
		    return true; 
		}
		return super.onOptionsItemSelected(item);
	}

	public List<ApplicationInfo> getAppList() {
		return appList;
	}

	public void setAppList() {
		appList = new ArrayList<ApplicationInfo>();
		for(String appPackageName : hmaDBHelper.readAppPackageNames(hmaDB)) {
			try {
				if(appPackageName != null) {
					appList.add(packageManager.getApplicationInfo(appPackageName, 
							PackageManager.GET_META_DATA 
							| PackageManager.GET_SHARED_LIBRARY_FILES 
							| PackageManager.GET_UNINSTALLED_PACKAGES));
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}