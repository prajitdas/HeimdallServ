/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umbc.cs.ebiquity.android.mithril.hma.webservice;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author sandeep
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface DataManager {
    public String printString(String inp);
    public String pollForUninstall(String username, String email, String deviceid);
    
}
