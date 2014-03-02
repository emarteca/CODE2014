<?php
	$userid = $_POST["userid"];
	
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "SELECT * FROM codeUsers WHERE id=$userid";
	$result=mysql_query($query);
	$num = mysql_num_rows($result);
	$output = "";
	if($num == 0){
		echo ("fail");
	}else{
		for($i=0;$i<$num;$i++){
			$output = $output . mysql_result($result,$i,"username") . ",";
			$output = $output . "" . ",";
			$output = $output . mysql_result($result,$i,"colorR") . ",";
			$output = $output . mysql_result($result,$i,"colorG") . ",";
			$output = $output . mysql_result($result,$i,"colorB") . ",";
			$output = $output . mysql_result($result,$i,"claims") . ",";
			$output = $output . mysql_result($result,$i,"score") . ",";
		}
		echo $output;
	}
	
	
?>