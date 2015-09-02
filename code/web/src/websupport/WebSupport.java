/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websupport;

import edu.umbc.cs.ebiquity.android.mithril.hma.server.Storage;
import edu.umbc.cs.ebiquity.android.mithril.hma.webservice.DataManagerImpl;
import javax.xml.ws.Endpoint;

/**
 *
 * @author sandeep
 */
public class WebSupport {

    /**
     * @param args the command line arguments
     */
    public static Storage storage = null;
    public static String output = "AppAn\\AdminDashBoard.html";
    public static void main(String[] args) {
        // TODO code application logic here
        storage = new Storage();
        Endpoint.publish("http://localhost:1234/ws/datamanager", new DataManagerImpl());
        
    }
    
}
