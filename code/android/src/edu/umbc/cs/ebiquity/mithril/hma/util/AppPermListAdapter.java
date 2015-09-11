package edu.umbc.cs.ebiquity.mithril.hma.util;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.umbc.cs.ebiquity.mithril.hma.R;

public class AppPermListAdapter extends ArrayAdapter<PermissionInfo> {
	private List<PermissionInfo> permList;
	private Context context;
	private PackageManager packageManager;
	
	public AppPermListAdapter(Context context, int resource, List<PermissionInfo> objects) {
		super(context, resource, objects);

		this.context = context;
		this.permList = objects;
		packageManager = context.getPackageManager();
	}
	
	@Override
	public int getCount() {
		return ((permList != null) ? permList.size() : 0);
	}

	@Override
	public PermissionInfo getItem(int position) {
		return 	((permList != null) ? permList.get(position) : null);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.app_detail_list_item, null);
		}
		
		PermissionInfo data = permList.get(position);
		
		if(data != null) {
			TextView permissionName = (TextView) view.findViewById(R.id.perm_name);
			TextView permissionDescription = (TextView) view.findViewById(R.id.perm_description);
			TextView permissionProtectionLevel = (TextView) view.findViewById(R.id.protection_level);
			TextView permissionGroup = (TextView) view.findViewById(R.id.perm_group);
			
			permissionName.setText(data.loadLabel(packageManager));

			if(data.loadDescription(packageManager) != null)
				permissionDescription.setText(data.loadDescription(packageManager));
			else
				permissionDescription.setText(R.string.no_description_available_txt);
			
			String protctionLevel = new String();
			
			switch(data.protectionLevel) {
				case PermissionInfo.PROTECTION_NORMAL:
			        protctionLevel = "normal";
			        break; 
			    case PermissionInfo.PROTECTION_DANGEROUS:
			        protctionLevel = "dangerous";
			        break; 
			    case PermissionInfo.PROTECTION_SIGNATURE:
			        protctionLevel = "signature";
			        break; 
			    case PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM:
			        protctionLevel = "signatureOrSystem";
			        break; 
			    case PermissionInfo.PROTECTION_FLAG_SYSTEM:
			        protctionLevel = "system";
			        break; 
			    default: 
			        protctionLevel = "<unknown>";
			        break;
			}
			
			permissionProtectionLevel.setText(protctionLevel);
			
			if(data.group != null)
				permissionGroup.setText(data.group);
		}
		return view;
	}
}