<?php
$lon = $_POST["lon"];
$lat = $_POST["lat"];
?>
<html>
	<head>
		<title>WEBSITESSSSSS</title>
		<script src="jquery-1.11.0.js"></script>
		<script>
			// ==ClosureCompiler==
			// @output_file_name default.js
			// @compilation_level SIMPLE_OPTIMIZATIONS
			// ==/ClosureCompiler==
			
			function start(latitude, longitude)		//lat = x, lon = y
			{
			
				unicorns = latitude;
				dragons = longitude;

				bottomLeft= [0, 0];
	
				$.getJSON("bsCorner.json", function( data ){	//data is json obj
					$.each( data, function( key, val ) {
						if(key == "bottomLeft")
						{
							bottomLeft = val;
						}
						else if (key == "topLeft")
						{
							topLeft = val;	
						}
						else if (key == "topRight")
						{
							topRight = val;
						}
						else if(key == "elevationArray")
						{
							mapData = val;
							init();
						}
					} 
				)});
			}

			function peakTest()
			{
			
			numPeaks = 0;
				for(p = 0; p < 262; p++)
				{
					for(q = 0; q < 262; q++)
					{
						if(isPeak(p, q) == true)
							numPeaks++;
						
					}
				}
			//document.write("The number of peaks peaked at: " + numPeaks);
			}
			
			function init()
			{
				
				tLLat = topLeft[0];
				tLLon = topLeft[1];

				mapH = Math.abs(topLeft[1] - bottomLeft[1]);

				mapW = Math.abs(topLeft[0] - topRight[0]);


				pixelW = 263;		//264 - 1
				pixelH = 263;

				LON_TO_PIXEL = Math.abs(mapH/263);	
				LAT_TO_PIXEL = Math.abs(mapW/263);
				
				someX = lonToX(xDist(unicorns));
				someY = latToY(yDist(dragons));
				
				//document.write(isPeak(someX, someY ));
				
			}

			function isPeak(x, y) {	
				
				tol = 2;
				
				check = mapData[x][y];
				
				for (i = 1; i <= tol; i ++) {
					isAPeak = true;
					if (x+i < pixelH) {
						if (mapData[x+i][y] > check || (i == tol && mapData[x+i][y] == check))
							return false;
						else if (mapData[x+i][y] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
					if (y+i < pixelW) {
						if (mapData[x][y+i] > check || (i == tol && mapData[x][y+i] == check))
							return false;
						else if (mapData[x][y+i] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
					if (x+i < pixelH && y+i < pixelW) {
						if (mapData[x+i][y+i] > check || (i == tol && mapData[x+i][y=i] == check))
							return false;
						else if (mapData[x+i][y+i] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
						
					if (x-i >= 0) {
						if (mapData[x-i][y] > check || (i == tol && mapData[x-i][y] == check))
							return false;
						else if (mapData[x-i][y] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
					if (y-i >= 0) {
						if (mapData[x][y-i] > check || (i == tol && mapData[x][y-i] == check))
							return false;
						else if (mapData[x][y-i] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
					if (x-i >= 0 && y-i >= 0) {
						if (mapData[x-i][y-i] > check || (i == tol && mapData[x-i][y-i] == check))
							return false;
						else if (mapData[x-i][y-i] < check)
							isAPeak = isAPeak && true;
						else
							isAPeak = isAPeak && false;
					}
					
					if(isAPeak) return true;
					
				}
				
				return true;
			}

			
			
			
			function tester()
			{
				alert(mapData[0][0]);
			}
			
			function lonToX(lonDist)
			{
				pixelDiff = lonDist/LON_TO_PIXEL;
				
				return pixelDiff;
			}

			function latToY(latDist)
			{
				pixelDiff = latDist/LAT_TO_PIXEL;
				return pixelDiff;
			}

			function yDist(latPos)
			{
				dist = Math.abs(latPos - tLLat);
				if(dist > 0.5) dist = -1;

				return dist;
			}

			function xDist(lonPos)
			{
				dist = Math.abs(lonPos - tLLon);

				if(dist > 0.25) dist = -1;

				return dist;
			}
			
			function getHeight(x1, y1)
			{
				//document.write(mapData[x1][y1]);
			}
					
		</script>
	</head> 
	<body onload="start(<?php echo($lon.",".$lat); ?>)">
	</body>
</html>