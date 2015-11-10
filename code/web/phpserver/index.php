
<?php
error_reporting(E_ALL ^ E_DEPRECATED);
$dbusername = "root";
$dbpassword = "";
$dbhostname = "localhost";

$dbhandle = mysql_connect($dbhostname, $dbusername, $dbpassword)
    or die("Unable to connect to MySQL");
$selected = mysql_select_db("hma",$dbhandle)
    or die("Could not selected db2");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $approval = $_POST["action"];

    $result =  mysql_query("SELECT * FROM deviceinfo");
    while($row = mysql_fetch_array($result)) {

        $unamedb = $row{'username'};
        $emaildb = $row{'email'};
        $deviceiddb = $row{'deviceid'};


        if (strcmp($unamedb, $_POST["username"]) == 0 and 
                strcmp($emaildb, $_POST["email"]) == 0 and 
                strcmp($deviceiddb, $_POST["deviceid"]) == 0) {

            if (strcmp($approval, "Approve") == 0) {
                $app_cons = $_POST["appname_consid"];
                mysql_query("DELETE FROM addedapplications WHERE deviceid = '$deviceiddb' AND appname = '$app_cons'");


            } elseif (strcmp($approval, "Reject") == 0) {
                $app_cons = $_POST["appname_consid"];
                mysql_query("DELETE FROM addedapplications WHERE deviceid = '$deviceiddb' AND appname = '$app_cons'");
                mysql_query("INSERT INTO removedapplications (deviceid, appname) values ('$deviceiddb', '$app_cons')");
            } 
        }
    }
}

?>
<!DOCTYPE html>
<!-- saved from url=(0043)http:// -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="10"; URL=".">    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://getbootstrap.com/favicon.ico">

    <title>Heimdall Dashboard</title>

    <!-- Bootstrap core CSS -->
    <link href="http://getbootstrap.com/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="http://getbootstrap.com/examples/dashboard/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="./assets/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="http://getbootstrap.com/examples/dashboard/#">Heimdall Inc.</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href=".">Dashboard</a></li>
            <li><a href=".">Settings</a></li>
            <li><a href=".">Profile</a></li>
            <li><a href=".">Help</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href=".">Overview <span class="sr-only">(current)</span></a></li>
            <li><a href="http://eb4.cs.umbc.edu/forceclusters.php?appid=com.healthifyme.basic">Reports</a></li>
            <li><a href="http://eb4.cs.umbc.edu/forceclusters.php?appid=com.healthifyme.basic">Analytics</a></li>
            <li><a href="http://eb4.cs.umbc.edu/forceclusters.php?appid=com.healthifyme.basic">Export</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">Dashboard</h1>
<div class="well hero-unit">
          <div class="row placeholders">
            <div class="col-xs-6 col-sm-3 placeholder">
              <img HEIGHT=100 WIDTH=100 src= "assets/androidApp.png" class="img-responsive" alt="Generic placeholder thumbnail">
              <h4>Overall User Statistics</h4>
              <!--<span class="text-muted">Complete </span>-->
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <img HEIGHT=100 WIDTH=100 src= "assets/androidApp.png" class="img-responsive" alt="Generic placeholder thumbnail">
              <h4>Today's User Statistics</h4>
              <!--<span class="text-muted">UMBC</span>-->
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <img HEIGHT=100 WIDTH=100 src= "assets/androidApp.png" class="img-responsive" alt="Generic placeholder thumbnail">
              <h4>Security Events</h4>
              <!--<span class="text-muted">UMBC</span>-->
            </div>
            <div class="col-xs-6 col-sm-3 placeholder">
              <img HEIGHT=100 WIDTH=100 src= "assets/androidApp.png" class="img-responsive" alt="Generic placeholder thumbnail">
              <h4>Actions Recommended</h4>
              <!--<span class="text-muted">UMBC</span>-->
            </div>
          </div>
    <div class="text-muted"><h4>Hi, Admin</h4>
        <p>Welcome to Heimdall. There are no security events which require your urgent attention. You have <a href= "">3 meetings</a> scheduled for today.  </p></div>
        </div>
          <h2 class="sub-header">Admin Panel</h2>
          <div class="table-responsive">
            <table class="table table-striped table-condensed">
                <thead>
                    <tr class="active">
                  <th>User Name</th>
                  <th>Email </th>
                  <th>DeviceID </th>
                  <th>New Application Alert</th>
                  <th>Applications Marked for deletion</th>
                  <th>All Installed Applications</th>
                </tr>
              </thead>
              <tbody>

<?php

$inresult = mysql_query("SELECT * FROM deviceinfo");
$count = 0;
while($row = mysql_fetch_array($inresult)) {
  $count = $count + 1;
  
  $username = $row{'username'};
  $email = $row{'email'};
  $deviceid = $row{'deviceid'};
  $allappsQuery = mysql_query("SELECT * FROM applications WHERE deviceid = '$deviceid'");
    

  $installed_apps = array(); 
  while ($installed_apps_row = mysql_fetch_array($allappsQuery)) {
    array_push($installed_apps, $installed_apps_row{'appname'});
  }


  $addedappsQuery = mysql_query("SELECT * FROM addedapplications WHERE deviceid = '$deviceid'");
    

  $added = array(); 
  while ($added_apps_row = mysql_fetch_array($addedappsQuery)) {
    array_push($added, $added_apps_row{'appname'});
  }

  $removedappsQuery = mysql_query("SELECT * FROM removedapplications WHERE deviceid = '$deviceid'");
    

  $removed = array(); 
  while ($removed_apps_row = mysql_fetch_array($removedappsQuery)) {
    array_push($removed, $removed_apps_row{'appname'});
  }


  

?>






<tr>


<td>


    <?php
    echo $username;
    ?>
</td>
<td>
    <?php
    echo $email;
    ?>
</td>
<td>
    <?php
    echo $deviceid;
    ?>
</td>





<td class = "pagination-centered text-centered">
                        <div class="btn-group">
                            <a class="btn" href="."><i class="icon-user"></i> Apps</a>
                            <a class="btn dropdown-toggle" href="." data-toggle="dropdown">
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">

    <?php
    
    foreach ($added as $this_added) {
        if(strcmp($this_added, "added") == 0) {
            continue;
        }

    
    ?>


   <li><a href="http://eb4.cs.umbc.edu/forceclusters.php?appid=<?php echo $this_added; ?>">
    <?php  echo $this_added ?>                   
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
    <input type="hidden" name="username" value="<?php echo "$username"; ?>" > 
    <input type="hidden" name="email" value="<?php echo "$email"; ?>">
    <input type="hidden" name="deviceid" value="<?php echo "$deviceid"; ?>">
    <input type="hidden" name="appname_consid" value="<?php echo "$this_added"; ?>">
    <input type="submit" name="action" value="Approve">
    <input type="submit" name="action" value="Reject">
</form>

                            </a></li><li class="divider"></li>


<?php } ?>
            </ul>
        </div>
</td>

   

<td class = "pagination-centered text-centered">
                        <div class="btn-group">
                            <a class="btn" href="."><i class="icon-user"></i> Apps</a>
                            <a class="btn dropdown-toggle" href="." data-toggle="dropdown">
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">

    <?php
    foreach ($removed as $this_removed) {
        if(strcmp($this_removed, "removed") == 0) {
            continue;
        }
    ?>


                            <li><a href=".">
    <?php  echo $this_removed ?>                   


                            </a></li><li class="divider"></li>


<?php } ?>
            </ul>
        </div>
</td>


<td class = "pagination-centered text-centered">
                        <div class="btn-group">
                            <a class="btn" href="."><i class="icon-user"></i> Apps</a>
                            <a class="btn dropdown-toggle" href="." data-toggle="dropdown">
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
<?php 
    foreach ($installed_apps as $this_app) {
?>

                            <li><a href=".">
    <?php  echo $this_app ?>                   
                            </a></li><li class="divider"></li>


<?php } ?>
            </ul>
        </div>
</td>



</tr>

<?php            
}


for ($i = $count; $i < 20; $i++) {
?>
<tr>
<td></br></td>
<td></br></td>
<td></br></td>
<td></br></td>
<td></br></td>
<td></br></td>
</tr>
<?php
}


?>



            </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./assets/jquery.min.js"></script>
    <script src="./assets/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="./assets/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="./assets/ie10-viewport-bug-workaround.js"></script>
  

</body>
</html>