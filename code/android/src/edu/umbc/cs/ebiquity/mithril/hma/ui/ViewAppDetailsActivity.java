package edu.umbc.cs.ebiquity.mithril.hma.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import edu.umbc.cs.ebiquity.mithril.hma.HMAApplication;
import edu.umbc.cs.ebiquity.mithril.hma.R;
import edu.umbc.cs.ebiquity.mithril.hma.util.AppPermListAdapter;
import edu.umbc.cs.ebiquity.mithril.hma.util.HMADBHelper;

public class ViewAppDetailsActivity extends ListActivity {
	private PackageManager packageManager;
	private List<PermissionInfo> appPermList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_app_details);
		
		initView();
	}
	
	private void initView() {
		packageManager = getApplicationContext().getPackageManager();
		setAppPermList(getIntent().getStringExtra(HMADBHelper.getAppPackageName()));
		// Bind to new adapter. 
		setListAdapter(new AppPermListAdapter(ViewAppDetailsActivity.this, R.layout.app_list_item, getAppPermList()));
	}

	public List<PermissionInfo> getAppPermList() {
		return appPermList;
	}

	public void setAppPermList(String packageName) {
		appPermList = new ArrayList<PermissionInfo>();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
			for(PermissionInfo permissionInfo : packageInfo.permissions) {
				appPermList.add(permissionInfo);
				Log.i(HMAApplication.getDebugTag(), permissionInfo.loadLabel(packageManager).toString());
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}