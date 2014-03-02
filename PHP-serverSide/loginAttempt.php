<?php
	$user = $_POST["user"];
	$pass = $_POST["pass"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "SELECT * FROM codeUsers WHERE username='$user' AND password='$pass'";
	$result = mysql_query($query);
		if(mysql_affected_rows() == 1){
			echo "success," .  mysql_result($result,0,"id");
		}else{
			echo "fail" . $user . " " . $pass;
		}
?>