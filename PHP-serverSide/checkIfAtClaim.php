<?php
	$playerX = $_POST["xTile"];
	$playerY = $_POST["yTile"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "SELECT * FROM claims WHERE xTile=$playerX AND yTile=$playerY";
	$result=mysql_query($query);
	$num=mysql_num_rows($result);
	$output = "";
	mysql_query($query);
		if($num == 1){
			echo"success";
		}else{
			echo"fail" . $playerX . " " . $playerY;
		}
	echo $output;
	
?>