/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umbc.cs.ebiquity.android.mithril.hma.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author sandeep
 */
public class Storage {
    private ArrayList<String> storageContents = new ArrayList<>();
    public HashMap<String,ArrayList<String>> contents = new HashMap<>();
    public HashMap<String,String> newAppInfo = new HashMap<>();
    private String permanentStorage = "per.storage";
    
    public Storage() {
        try {
            GetStoredDataFromStorage();
            getParsedData();
        } catch (ParseException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public AppContents getAppContentsFromJson(String line) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jobjName = (JSONObject)jsonParser.parse(line);
        AppContents appContents = new AppContents();
        appContents.userName = (String)jobjName.get("identity");
        appContents.deviceID = (String)jobjName.get("deviceId");
        JSONArray appList = (JSONArray)jobjName.get("currentApps");
        appContents.modifiedApp = (String)jobjName.get("modifiedApp");
        for (int j = 0; j < appList.size(); j++) {
            appContents.appList.add((String)appList.get(j));
        }
        return appContents;
    }
    
    public void putOrUpdateStorage(String line) {
        try {
            putOrUpdateStorage(getAppContentsFromJson(line));
        } catch (ParseException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String getUniqueID(String userName, String deviceId) {
        return userName + ":" + deviceId;
    }
    public void putOrUpdateStorage(AppContents appContents) {
        String userName = appContents.userName;
        String deviceID = appContents.deviceID;
        String uniqueID = getUniqueID(userName, deviceID);
        ArrayList<String> installedAppsStorage = contents.get(uniqueID);
        System.out.println("New App Installed : " + appContents.modifiedApp);
        if (appContents.modifiedApp.equals("null"))
            return;
        //System.out.println("not here");
        if (contents.containsKey(uniqueID)) {
            ArrayList<String> installedAppsNew = appContents.appList;
            newAppInfo.put(uniqueID, appContents.modifiedApp);
            for (int i = 0; i < installedAppsNew.size(); i++) {
                Boolean found = Boolean.FALSE;
                String thisApp = installedAppsNew.get(i);
                for (int j = 0; j < installedAppsStorage.size(); j++) {
                    if (thisApp == installedAppsStorage.get(j)) {
                        found = Boolean.TRUE;
                        break;
                    }
                }
                if (!found) {
                    installedAppsStorage.add(thisApp);
                }
            }
            contents.put(uniqueID, installedAppsStorage);
        }
        else {
            contents.put(uniqueID, appContents.appList);
            newAppInfo.put(uniqueID, appContents.modifiedApp);
        }
    }
    
    public void getParsedData() throws ParseException {
        
        for (int i = 0; i < storageContents.size(); i ++) {
            AppContents appContents = getAppContentsFromJson(storageContents.get(i));
            String uniqueID = getUniqueID(appContents.userName, appContents.deviceID);
            contents.put(uniqueID, appContents.appList);
            newAppInfo.put(uniqueID, appContents.modifiedApp);
        }
    }
    
    public void displayStorage() {
        Iterator it = contents.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry thisEntry = (Map.Entry < String, ArrayList<String>>)it.next();
            //System.out.println(thisEntry.getKey() + " - " + thisEntry.getValue().toString());
        }
    }
    public String getStorage(String seperator) {
        Iterator it = contents.entrySet().iterator();
        String retString = "";
        int countLines = 0;
        while (it.hasNext()) {
            countLines ++;
            Map.Entry thisEntry = (Map.Entry < String, ArrayList<String>>)it.next();
            String uniqueID = (String) thisEntry.getKey();
            String[] strArr = uniqueID.split(":");
            String deviceID = strArr[1];
            String email = strArr[0];
            String name = email;
            if (email.contains("@")) {
                name = email.substring(0, email.indexOf("@"));
            }
            
            retString = retString + "<tr>"
                    + "<td>" 
                    + name 
                    + "</td>";
            retString = retString 
                    + "<td>" 
                    + email 
                    + "</td>";
            retString = retString 
                    + "<td>"
                    + deviceID
                    + "</td>";
            retString = retString 
                    + "<td>"
                    + "<li><a href=\""
                    + "http://eb4.cs.umbc.edu/forceclusters.php?appid="
                    + newAppInfo.get(uniqueID).replaceAll("\\.", "-")
                    + "\">"
                    + newAppInfo.get(uniqueID)
                    + "</a></li>"
                    + "</td>";
                    
            retString = retString +
//                    "<td  class=\"user-actions\">\n" +
//"                          <span>\n" +
//"                            <a class=\"label label-success\" href=\"javascript:void(0);\">....Info...</a> \n" +
//"                          </span>\n" +
//"                      </td>" +
                    "<td class = \"pagination-centered text-centered\">\n" +
"                        <div class=\"btn-group\">\n" +
"                            <a class=\"btn\" href=\".\"><i class=\"icon-user\"></i> Apps</a>\n" +
"                            <a class=\"btn dropdown-toggle\" href=\".\" data-toggle=\"dropdown\">\n" +
"                                <span class=\"caret\"></span>\n" +
"                            </a>\n" +
"                            <ul class=\"dropdown-menu\">";
            
            ArrayList<String> applist = (ArrayList<String>)thisEntry.getValue();
            retString = retString  
                    + "<li><a href=\".\">"
                    + applist.get(0)
                    + "</a></li>";
            
            for (int i = 1; i < ((ArrayList<String>)thisEntry.getValue()).size(); i++) {
                retString = retString 
                        + "<li class=\"divider\"></li>"
                    + "<li><a href=\".\">"
                    + applist.get(i)
                    + "</a></li>";
                
            }
            retString = retString +
                    "</ul>\n" +
"                        </div>\n" +
"                      </td>\n" +
"                  </tr>";
        }
        for (int i = countLines; i < 20; i ++) {
            retString = retString + "<tr><td>.</td><td></td><td></td><td></td><td></td></tr>";
        }
        return retString;
    }
    
    
    private void GetStoredDataFromStorage() {
        readFile(permanentStorage);
    }
    
    private void readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String test = "";
            while((test = br.readLine()) != null) {
                storageContents.add(test);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeFile(String fileName, ArrayList<String> fileContents) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            OutputStreamWriter output = new OutputStreamWriter(fos);
            for (int i = 0; i < fileContents.size(); i++) {
                output.write(fileContents.get(i));
            }
            output.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
