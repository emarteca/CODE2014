<?php
	$username="codeHackatho2014";
	$password="StarCon5!";
	$database="codeHackatho2014";
	
	mysql_connect("173.201.88.135",$username,$password);
	
	@mysql_select_db($database) or die( "Could not select database");
	
	$query = "SELECT * FROM claims";
	$result = mysql_query($query);
	$num=mysql_num_rows($result);
	$output = "DOLLAH";
	if(mysql_affected_rows() > 0){
		for($i=0; $i<$num; $i++){
			$output = $output . mysql_result($result,$i,"name") . ",";
			$output = $output . mysql_result($result,$i,"height") . ",";
			$output = $output . mysql_result($result,$i,"ownername") . ",";
			$output = $output . mysql_result($result,$i,"lat") . ",";
			$output = $output . mysql_result($result,$i,"lon") . ",";
			$output = $output . mysql_result($result,$i,"worth") . ",";
			$output = $output . "#";
		}
	}else{
		echo"fail";
	}
	echo $output;
	
?>