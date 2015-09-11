package edu.umbc.cs.ebiquity.mithril.hma.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.util.AppPermListAdapter;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class ViewAppDetailsActivity extends ListActivity {
	private PackageManager packageManager;
	private List<PermissionInfo> appPermList;
	private AppPermListAdapter appPermListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * ListActivity has a default layout that consists of a single, full-screen list in the center of the screen. 
		 * However, if you desire, you can customize the screen layout by setting your own view layout with setContentView() in onCreate(). 
		 * To do this, your own view MUST contain a ListView object with the id "@android:id/list" (or list if it's in code)
		 */
		setContentView(R.layout.activity_view_app_details);
		
		initView();
	}
	
	private void initView() {
		packageManager = getApplicationContext().getPackageManager();

		String appPkgName = getIntent().getStringExtra(HMADBHelper.getAppPackageName());
		HMADBHelper hmaDBHelper = new HMADBHelper(this);
		SQLiteDatabase hmaDB = hmaDBHelper.getWritableDatabase();
		setTitle(getResources().getText(R.string.title_activity_view_app_details)+": "+hmaDBHelper.readAppNameByPackageName(hmaDB, appPkgName));
		
		setAppPermList(appPkgName);
		
		appPermListAdapter = new AppPermListAdapter(ViewAppDetailsActivity.this, R.layout.app_list_item, getAppPermList());
		// Bind to new adapter. 
		if(appPermListAdapter.getCount() == 0)
			appPermListAdapter = null;
		setListAdapter(appPermListAdapter);
	}
	
	public List<PermissionInfo> getAppPermList() {
		return appPermList;
	}

	public void setAppPermList(String packageName) {
		appPermList = new ArrayList<PermissionInfo>();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
			if(packageInfo.permissions != null)
				for(PermissionInfo permissionInfo : packageInfo.permissions)
					if(permissionInfo != null)
						appPermList.add(permissionInfo);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}