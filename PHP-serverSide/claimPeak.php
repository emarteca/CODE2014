<?php
	$lat = $_POST["lat"];
	$lon = $_POST["lon"];
	$name = $_POST["name"];
	$height = $_POST["height"];
	$ownername = $_POST["ownername"];
	$x = $_POST["xTile"];
	$y = $_POST["yTile"];
	$worth = $_POST["worth"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "INSERT INTO claims (claimid,name,height,ownername,lat,lon,xTile,yTile,worth) VALUES (NULL, '$name', '$height', '$ownername', '$lat', '$lon', '$x', '$y', '$worth')";
	mysql_query($query);
	if(mysql_affected_rows() > 0){
		echo"success";
	}else{
		echo"fail";
	}
	echo $output;
	
?>