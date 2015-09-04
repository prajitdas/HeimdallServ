package edu.umbc.cs.ebiquity.android.mithril.hma.server;


import edu.umbc.cs.ebiquity.android.mithril.hma.server.Storage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sandeep
 */
public class GenerateHTML {
    public ArrayList<String> storageContents = new ArrayList<>();
//   / pubilc String templateFile = "";
    public GenerateHTML(String input, String outputFileName) {
        String html = "<!DOCTYPE html>\n" +
"<!-- saved from url=(0043)http:// -->\n" +
"<html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
"<meta http-equiv=\"refresh\" content=\"100\"; URL=\".\">    <meta charset=\"utf-8\">\n" +
"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
"    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
"    <meta name=\"description\" content=\"\">\n" +
"    <meta name=\"author\" content=\"\">\n" +
"    <link rel=\"icon\" href=\"http://getbootstrap.com/favicon.ico\">\n" +
"\n" +
"    <title>Heimdall Dashboard</title>\n" +
"\n" +
"    <!-- Bootstrap core CSS -->\n" +
"    <link href=\"http://getbootstrap.com/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
"\n" +
"    <!-- Custom styles for this template -->\n" +
"    <link href=\"http://getbootstrap.com/examples/dashboard/dashboard.css\" rel=\"stylesheet\">\n" +
"\n" +
"    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->\n" +
"    <!--[if lt IE 9]><script src=\"../../assets/js/ie8-responsive-file-warning.js\"></script><![endif]-->\n" +
"    <script src=\"./AdminDashboard_files/ie-emulation-modes-warning.js\"></script>\n" +
"\n" +
"    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n" +
"    <!--[if lt IE 9]>\n" +
"      <script src=\"https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>\n" +
"      <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n" +
"    <![endif]-->\n" +
"  </head>\n" +
"\n" +
"  <body>\n" +
"\n" +
"    <nav class=\"navbar navbar-inverse navbar-fixed-top\">\n" +
"      <div class=\"container-fluid\">\n" +
"        <div class=\"navbar-header\">\n" +
"          <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" aria-controls=\"navbar\">\n" +
"            <span class=\"sr-only\">Toggle navigation</span>\n" +
"            <span class=\"icon-bar\"></span>\n" +
"            <span class=\"icon-bar\"></span>\n" +
"            <span class=\"icon-bar\"></span>\n" +
"          </button>\n" +
"          <a class=\"navbar-brand\" href=\"http://getbootstrap.com/examples/dashboard/#\">Heimdall Inc.</a>\n" +
"        </div>\n" +
"        <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
"          <ul class=\"nav navbar-nav navbar-right\">\n" +
"            <li><a href=\".\">Dashboard</a></li>\n" +
"            <li><a href=\".\">Settings</a></li>\n" +
"            <li><a href=\".\">Profile</a></li>\n" +
"            <li><a href=\".\">Help</a></li>\n" +
"          </ul>\n" +
"          <form class=\"navbar-form navbar-right\">\n" +
"            <input type=\"text\" class=\"form-control\" placeholder=\"Search...\">\n" +
"          </form>\n" +
"        </div>\n" +
"      </div>\n" +
"    </nav>\n" +
"\n" +
"    <div class=\"container-fluid\">\n" +
"      <div class=\"row\">\n" +
"        <div class=\"col-sm-3 col-md-2 sidebar\">\n" +
"          <ul class=\"nav nav-sidebar\">\n" +
"            <li class=\"active\"><a href=\".\">Overview <span class=\"sr-only\">(current)</span></a></li>\n" +
"            <li><a href=\"http://userpages.umbc.edu/~law8/malapp/forceclusters.html\">Reports</a></li>\n" +
"            <li><a href=\"http://userpages.umbc.edu/~law8/malapp/forceclusters.html\">Analytics</a></li>\n" +
"            <li><a href=\"http://userpages.umbc.edu/~law8/malapp/forceclusters.html\">Export</a></li>\n" +
"          </ul>\n" +
"        </div>\n" +
"        <div class=\"col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main\">\n" +
"          <h1 class=\"page-header\">Dashboard</h1>\n" +
"<div class=\"well hero-unit\">\n" +
"          <div class=\"row placeholders\">\n" +
"            <div class=\"col-xs-6 col-sm-3 placeholder\">\n" +
"              <img HEIGHT=100 WIDTH=100 src= \"AdminDashboard_files/AndroidApp.png\" class=\"img-responsive\" alt=\"Generic placeholder thumbnail\">\n" +
"              <h4>Overall User Statistics</h4>\n" +
"              <!--<span class=\"text-muted\">Complete </span>-->\n" +
"            </div>\n" +
"            <div class=\"col-xs-6 col-sm-3 placeholder\">\n" +
"              <img HEIGHT=100 WIDTH=100 src= \"AdminDashboard_files/AndroidApp.png\" class=\"img-responsive\" alt=\"Generic placeholder thumbnail\">\n" +
"              <h4>Today's User Statistics</h4>\n" +
"              <!--<span class=\"text-muted\">UMBC</span>-->\n" +
"            </div>\n" +
"            <div class=\"col-xs-6 col-sm-3 placeholder\">\n" +
"              <img HEIGHT=100 WIDTH=100 src= \"AdminDashboard_files/AndroidApp.png\" class=\"img-responsive\" alt=\"Generic placeholder thumbnail\">\n" +
"              <h4>Security Events</h4>\n" +
"              <!--<span class=\"text-muted\">UMBC</span>-->\n" +
"            </div>\n" +
"            <div class=\"col-xs-6 col-sm-3 placeholder\">\n" +
"              <img HEIGHT=100 WIDTH=100 src= \"AdminDashboard_files/AndroidApp.png\" class=\"img-responsive\" alt=\"Generic placeholder thumbnail\">\n" +
"              <h4>Actions Recommended</h4>\n" +
"              <!--<span class=\"text-muted\">UMBC</span>-->\n" +
"            </div>\n" +
"          </div>\n" +
"    <div class=\"text-muted\"><h4>Hi, Admin</h4>\n" +
"        <p>Welcome to Heimdall. There are no security events which require your urgent attention. You have <a href= \"\">3 meetings</a> scheduled for today.  </p></div>\n" +
"        </div>\n" +
"          <h2 class=\"sub-header\">Admin Panel</h2>\n" +
"          <div class=\"table-responsive\">\n" +
"            <table class=\"table table-striped table-condensed\">\n" +
"                <thead>\n" +
"                    <tr class=\"active\">\n" +
"                  <th>User Name</th>\n" +
"                  <th>Email </th>\n" +
"                  <th>DeviceID </th>\n" +
"                  <th>New Application</th>\n" +
"                  <th>All Installed Applications</th>\n" +
"                </tr>\n" +
"              </thead>\n" +
"              <tbody>";
        html = html + input + 
                "</tbody>\n" +
"            </table>\n" +
"          </div>\n" +
"        </div>\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <!-- Bootstrap core JavaScript\n" +
"    ================================================== -->\n" +
"    <!-- Placed at the end of the document so the pages load faster -->\n" +
"    <script src=\"./AdminDashboard_files/jquery.min.js\"></script>\n" +
"    <script src=\"./AdminDashboard_files/bootstrap.min.js\"></script>\n" +
"    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->\n" +
"    <script src=\"./AdminDashboard_files/holder.min.js\"></script>\n" +
"    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->\n" +
"    <script src=\"./AdminDashboard_files/ie10-viewport-bug-workaround.js\"></script>\n" +
"  \n" +
"\n" +
"</body></html>";
         
        
        
        
        
        
        
//        String OutputHtmlFirst = "<html><head><meta http-equiv=\"refresh\" content=\"5; URL=\".\"></head>"
//                + "<body>";
//        
//        String OutputHtmlSecond = "</body></html>";
//        
        ArrayList<String> finalHTML = new ArrayList<>();
        finalHTML.add(html);
        
        writeFile(outputFileName, finalHTML);
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
