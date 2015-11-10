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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public HashMap<String,AppContents> contents = new HashMap<>();
//    public HashMap<String,String> newAppInfo = new HashMap<>();
    private String permanentStorage = "per.storage";
    private String prevApp = "";
    private Connection con = null;
    public Storage() {
        try {
            connectToDatabase();
            GetStoredDataFromStorage();
            getParsedData();
            
        } catch (ParseException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
        con = DriverManager.getConnection("jdbc:mysql://localhost/hma?", "root", "");
                                                                
        

    }
    public AppContents getAppContentsFromJson(String line) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jobjName = (JSONObject)jsonParser.parse(line);
        AppContents appContents = new AppContents();
        appContents.userName = (String)jobjName.get("identity");
        appContents.deviceID = (String)jobjName.get("deviceId");
        JSONArray appList = (JSONArray)jobjName.get("currentApps");
        appContents.modifiedApp = (String)jobjName.get("modifiedApp");
        if (((String)jobjName.get("installFlag")).equals("true")) {
            appContents.added = Boolean.TRUE;
            appContents.addedApp = (String)jobjName.get("modifiedApp");
        } else {
            appContents.added = Boolean.FALSE;
            appContents.removedApp = (String)jobjName.get("modifiedApp");
        }
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
    public void putOrUpdateStorageDB(String line) {
        try {
            putOrUpdateStorageDB(getAppContentsFromJson(line));
        } catch (ParseException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void putOrUpdateStorageDB(AppContents appContents) throws SQLException {
        String userName = appContents.userName;
        String deviceID = appContents.deviceID;
        String email = userName + "@gmail.com";
        String query = "SELECT * FROM deviceinfo WHERE username = '" + userName +"' AND email = '" + email + "' AND deviceid = '" + deviceID + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);;
        if (!rs.next()) {
            String insertQuery = "INSERT INTO deviceinfo (username, email, deviceid) VALUES ('" + userName + "', '" + email + "', '" + deviceID + "')";
            stmt.executeUpdate(insertQuery);
        }
        if (appContents.added) {
            String insertQuery = "INSERT INTO addedapplications (deviceid, appname) VALUES ('" + deviceID + "', '" + appContents.modifiedApp + "')";
            stmt.executeUpdate(insertQuery);
        } else {
            String removeQuery = "DELETE FROM removedapplications WHERE deviceid = '" + deviceID + "' AND appname = '" + appContents.modifiedApp + "'";
            stmt.executeUpdate(removeQuery);
        }
        String insertQuery = "INSERT INTO applications (deviceid, appname) VALUES ('" + appContents.deviceID + "', '" + appContents.appList.get(0) + "')";
        for (int i = 1; i < appContents.appList.size(); i++) {
            insertQuery = insertQuery + ", ('" + appContents.deviceID + "', '" + appContents.appList.get(i) + "')";
        }
        stmt.executeUpdate(insertQuery);
        
    }
        
    public ArrayList<String> getAppsToUninstall(String username, String email, String deviceid) {
        ArrayList<String> retArray = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT appname FROM removedapplications WHERE deviceid = '" + deviceid + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                retArray.add(rs.getString("appname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retArray;
    }
    
    public void putOrUpdateStorage(AppContents appContents) {
        String userName = appContents.userName;
        String deviceID = appContents.deviceID;
        String uniqueID = getUniqueID(userName, deviceID);
        if (contents.containsKey(uniqueID)) {
            AppContents thisAppCon = contents.get(uniqueID);
            if (appContents.added) {
                appContents.removedApp = thisAppCon.removedApp;
            } else {
                appContents.addedApp = thisAppCon.addedApp;
            }
            contents.put(uniqueID, appContents);
            
        }
        else {
            contents.put(uniqueID, appContents);
        }

    }
    
    public void getParsedData() throws ParseException {
        
        for (int i = 0; i < storageContents.size(); i ++) {
            AppContents appContents = getAppContentsFromJson(storageContents.get(i));
            String uniqueID = getUniqueID(appContents.userName, appContents.deviceID);
            contents.put(uniqueID, appContents);
//            newAppInfo.put(uniqueID, appContents.modifiedApp);
        }
    }
    
    public void displayStorage() {
        Iterator it = contents.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry thisEntry = (Map.Entry < String, ArrayList<String>>)it.next();
            System.out.println(thisEntry.getKey() + " - " + thisEntry.getValue().toString());
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
            
            String thisAdded = contents.get(uniqueID).addedApp;
            String thisRemoved = contents.get(uniqueID).removedApp;
            Boolean addedFlag = contents.get(uniqueID).added;
            if (thisAdded.equals("") || 
                    (thisAdded.equalsIgnoreCase(thisRemoved) && 
                      !addedFlag)) {
                retString = retString 
                    + "<td>"
                    + "<li>"
                    + "</li>"
                    + "</td>";
            } else {
                retString = retString 
                    + "<td>"
                    + "<li><a href=\""
                    + "http://eb4.cs.umbc.edu/forceclusters.php?appid="
                    + thisAdded.replaceAll("\\.", "-")
                    + "\">"
                    + thisAdded
                    + "</a></li>"
                    + "</td>";
            }
            if (thisRemoved.equals("") || 
                    (thisRemoved.equalsIgnoreCase(thisAdded) && 
                      addedFlag)) {
                retString = retString 
                    + "<td>"
                    + "<li>"
                    + "</li>"
                    + "</td>";
            } else {
                retString = retString 
                    + "<td>"
                    + "<li><a href=\""
                    + "http://eb4.cs.umbc.edu/forceclusters.php?appid="
                    + thisRemoved.replaceAll("\\.", "-")
                    + "\">"
                    + thisRemoved
                    + "</a></li>"
                    + "</td>";
            }
            
            
                    
            retString = retString +
                    "<td class = \"pagination-centered text-centered\">\n" +
"                        <div class=\"btn-group\">\n" +
"                            <a class=\"btn\" href=\".\"><i class=\"icon-user\"></i> Apps</a>\n" +
"                            <a class=\"btn dropdown-toggle\" href=\".\" data-toggle=\"dropdown\">\n" +
"                                <span class=\"caret\"></span>\n" +
"                            </a>\n" +
"                            <ul class=\"dropdown-menu\">";
            
            ArrayList<String> applist = (ArrayList<String>)((AppContents)thisEntry.getValue()).appList;
            retString = retString  
                    + "<li><a href=\""
                    + "http://eb4.cs.umbc.edu/forceclusters.php?appid="
                    + applist.get(0).replaceAll("\\.", "-")
                    + "\">"
                    + applist.get(0)
                    + "</a></li>";
            
            for (int i = 1; i < applist.size(); i++) {
                retString = retString 
                        + "<li class=\"divider\"></li>"
                        + "<li><a href=\""
                        + "http://eb4.cs.umbc.edu/forceclusters.php?appid="
                        + applist.get(0).replaceAll("\\.", "-")
                        + "\">"
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
            retString = retString + "<tr><td>.</td><td></td><td></td><td></td><td></td><td></td></tr>";
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
