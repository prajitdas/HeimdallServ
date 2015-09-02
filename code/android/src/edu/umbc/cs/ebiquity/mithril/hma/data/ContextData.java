package edu.umbc.cs.ebiquity.mithril.hma.data;

import java.util.List;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.location.Location;
import android.util.Patterns;

public class ContextData {
	private String activity;
	private Location location;
	private String purpose;
	private long time;
	private String identity;
	private List<String> appsRunning;
	
	public ContextData(Context context, Location location) {
		setIdentity(context);
		setTime();
		setLocation(location);
		setPurpose("DefaultPurpose");
		setActivity("DefaultActivity");
	}

	private void setLocation(Location location) {
		this.location = location;
	}

	public String getActivity() {
		return activity;
	}

	public List<String> getAppsRunning() {
		return appsRunning;
	}

	public Location getLocation() {
		return location;
	}

	public String getPurpose() {
		return purpose;
	}

	public long getTime() {
		return time;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public void setAppsRunning(List<String> appsRunning) {
		this.appsRunning = appsRunning;
	}
	
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public void setTime() {
		time = System.currentTimeMillis();
	}

	public String toStringForAppsRunning() {
		StringBuilder temp = new StringBuilder();
		for(String appRunning : appsRunning) {
			temp.append(appRunning);
			temp.append(",");
		}
		return temp.toString();
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(Context context) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
	    Account[] accounts = AccountManager.get(context).getAccounts();
	    for (Account account : accounts)
	        if (emailPattern.matcher(account.name).matches())
	        	identity = account.name;//possibleEmail
	}
}