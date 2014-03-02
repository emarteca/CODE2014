<?php
	$lat = $_POST["lat"];
	$lon = $_POST["lon"];
	$name = $_POST["name"];
	$height = $_POST["height"];
	$ownerid = $_POST["ownerid"];
	$worth = $_POST["worth"];
	$claimid = $_POST["claimid"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "UPDATE claims SET name='$name', height='$height', ownerid='$ownerid', lat='$lat', lon='$lon', worth='$worth' WHERE claimid='$claimid'";
	mysql_query($query);
	if(mysql_affected_rows() > 0){
		echo"success";
	}else{
		echo"fail";
	}
	echo $output;
	
?>