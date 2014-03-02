<?php
	$user = $_POST["user"];
	$pass = $_POST["pass"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$randR = rand(0, 255);
	$randG = rand(0, 255);
	$randB = rand(0, 255);
	
	$query = "INSERT INTO codeUsers (id,username,password,score,colorR,colorG,colorB,claims) VALUES (NULL, '$user', '$pass', '0', '$randR', '$randG', '$randB', '')";

	$result = mysql_query($query);
		if(mysql_affected_rows() == 1){
			echo "success";
		}else{
			echo "fail" . $user;
		}
?>