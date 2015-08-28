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
        JSONArray appList = (JSONArray)jobjName.get("appsInstalled");
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
    
    public void putOrUpdateStorage(AppContents appContents) {
        ArrayList<String> installedAppsStorage = contents.get(appContents.userName);
        if (contents.containsKey(appContents.userName)) {
            ArrayList<String> installedAppsNew = appContents.appList;
            
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
            contents.put(appContents.userName, installedAppsStorage);
        }
        else {
            contents.put(appContents.userName, appContents.appList);
        }
    }
    
    public void getParsedData() throws ParseException {
        
        for (int i = 0; i < storageContents.size(); i ++) {
            AppContents appContents = getAppContentsFromJson(storageContents.get(i));
            contents.put(appContents.userName, appContents.appList);
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
        while (it.hasNext()) {
            Map.Entry thisEntry = (Map.Entry < String, ArrayList<String>>)it.next();
            retString = retString + "<tr>"
                    + "<td>" 
                    + thisEntry.getKey() 
                    + "</td>";
            ArrayList<String> applist = (ArrayList<String>)thisEntry.getValue();
            retString = retString 
                    + "<td>" 
                    + applist.get(0)
                    + "</td>"
                    + "</tr>" ;
            for (int i = 1; i < ((ArrayList<String>)thisEntry.getValue()).size(); i++) {
                retString = retString 
                        + "<tr>"
                        + "<td>"
                        + "</td>"
                        + "<td>"
                        + applist.get(i)
                        + "</td>"
                        + "</tr>";           
            }
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
