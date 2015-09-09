/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umbc.cs.ebiquity.android.mithril.hma.webservice;

import edu.umbc.cs.ebiquity.android.mithril.hma.server.GenerateHTML;
import edu.umbc.cs.ebiquity.android.mithril.hma.server.Storage;
import javax.jws.WebService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author sandeep
 */
@WebService(endpointInterface = "edu.umbc.cs.ebiquity.android.mithril.hma.webservice.DataManager")
public class DataManagerImpl implements DataManager{
    @Override
    public String printString(String jsonInput) {
        System.out.println("hi prajit");
        websupport.WebSupport.storage.putOrUpdateStorage(jsonInput);
        websupport.WebSupport.storage.displayStorage();
        String htmlMid = websupport.WebSupport.storage.getStorage("</br>");
        GenerateHTML htmlPage = new GenerateHTML(htmlMid, websupport.WebSupport.output);
        
        return "";
    }
    
}
