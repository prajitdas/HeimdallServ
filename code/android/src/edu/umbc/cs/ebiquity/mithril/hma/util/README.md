#Contract with Webservice
Will send JSONObject with the following parameters:-
* "identity": AccountManager.get(context).getAccounts():account.name which matches Patterns.EMAIL_ADDRESS
* "modifiedApp": Recently modified app's package name
* "deviceId": Device Id obtained using:-
```java
private String getDeviceId() {
    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    
    final String tmDevice, tmSerial, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(
            context.getContentResolver(),
            android.provider.Settings.Secure.ANDROID_ID
            );
    
    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
    String deviceId = deviceUuid.toString();
    return deviceId;
}
```
* "installFlag" Denotes if the package was added to the device or was it changed/replaced/removed from the device
* "currentApps" jsonArray containing a list of apps that are currently installed on the device
