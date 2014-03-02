<?php
	$userid = $_POST["userid"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "SELECT friends FROM codeUsers WHERE id=$userid";
	$result=mysql_query($query);
	$num=mysql_numrows($result);
	$output = "";
	for($i=0;i<$num;$i++){
		$output = $output . mysql_result($result,$i,"name") . ",";
	}
	echo $output;
	
?>