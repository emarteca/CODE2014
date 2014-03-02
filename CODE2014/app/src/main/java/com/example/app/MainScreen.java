package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

//GPSTracker

public class MainScreen extends ActionBarActivity implements TimerListener, postListener, GoogleMap.OnMarkerDragListener{

    private Marker[] claimMarkers;

    private static GoogleMap theMap;
    private float MIN_ZOOM;
    private static LatLngBounds endBounds = new LatLngBounds(new LatLng(56.0, -129.5), new LatLng(56.25, -129.0));
    private LatLng startCoords = new LatLng(56.125, -129.25);

    private static Player player = new Player();

    private Marker pMark;

    private GPSTracker tracker;

    private double deltaLon;
    private double deltaLat;

    private PostRequester postman;

    private boolean useGPS = false;

    private Claim[] mapClaims;
    private int curHeight;

    private int counter = 0;

    //Data for map
    private static double tLLat = -129.5;					//ps y -> lon and x -> lat
    private static double tLLon = 56.25;

    private static double mapW = 0.5;
    private static double mapH = 0.25;

    private static double pixelW = 263;
    private static double pixelH = 263;

    private static final double LON_TO_PIXEL = 0.25/263;	//longitude corresponding to one pixel moved
    private static final double LAT_TO_PIXEL = 0.5/263;		//latitude	"	"	"	"	"	"	"	"

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        theMap.moveCamera(CameraUpdateFactory.newLatLngBounds(endBounds, 400,400,0));

        player.setLat(startCoords.latitude);
        player.setLon(startCoords.longitude);

        int COLOR2 = player.getPlayerColor();
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
        Drawable d = getResources().getDrawable(R.drawable.bonhomme);
        d.setColorFilter(COLOR2,mMode);
        Bitmap b = ((BitmapDrawable)d).getBitmap();

        pMark = theMap.addMarker(new MarkerOptions().position(startCoords).icon(BitmapDescriptorFactory.fromBitmap(b)).draggable(true));

        MIN_ZOOM = theMap.getCameraPosition().zoom;

        tracker = new GPSTracker(getApplicationContext());

        double trueLon = tracker.getLongitude();

        double trueLat = tracker.getLatitude();

        deltaLon = -trueLon + startCoords.longitude;
        deltaLat = -trueLat + startCoords.latitude;

        postman = new PostRequester(this, 0);

        //Timers R.I.P.
        BackgroundUpdater bp = new BackgroundUpdater();
        bp.init(this);

        setListeners();

        theMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        theMap.moveCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM*1.5f));

        theMap.setOnMarkerDragListener(this);

    }


    public void setListeners() {
        theMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom < MIN_ZOOM)
                    theMap.moveCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));

                double latitude = position.target.latitude;
                double longitude = position.target.longitude;

                updateCameraPosition(latitude, longitude);

            }
        });

    }

    private void resetClaims(){
        if (claimMarkers != null) {
            for (int i = 0; i < claimMarkers.length; i ++) {
                claimMarkers[i].remove();
                claimMarkers[i] = null;
            }
        }

        claimMarkers = new Marker[mapClaims.length];

        for (int i = 0; i < mapClaims.length; i ++) {
            LatLng pos = new LatLng(mapClaims[i].getLatitude(), mapClaims[i].getLongitude());
            claimMarkers[i] = theMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.drawable.flagmarker)).title(mapClaims[i].getName() + " - Owner: " + mapClaims[i].getOwner().getUsername() + " - Height: " + ((mapClaims[i].getHeight()/255.0)*1361)+210));
        }
    }

    public void startClaimCheck(double lat, double lon){
        postman = new PostRequester(this, 1);
        postman.newRequest("http://www.isaidso.ca/code/checkIfAtClaim.php",2);
        postman.addParam("xTile", "" + lonToX(xDist(player.getLat())));                    //(int) lonToX(xDist(testActLon))
        postman.addParam("yTile", "" + latToY(yDist(player.getLon())));
        postman.sendRequest();
    }

    public double[] fakeCoords(double lat, double lon)
    {
        double[] fakeCoords = new double[2];
        fakeCoords[0] = lat + deltaLat;
        fakeCoords[1] = lon + deltaLon;

        return fakeCoords;
    }

    public static double[] updateCameraPosition(double latitude, double longitude) {
        boolean toUpdate = false;
        if (latitude > endBounds.northeast.latitude) {
            latitude = endBounds.northeast.latitude;
            toUpdate = true;
        }
        else if (latitude < endBounds.southwest.latitude) {
            latitude = endBounds.northeast.latitude;
            toUpdate = true;
        }

        if (longitude > endBounds.northeast.longitude) {
            longitude = endBounds.northeast.longitude;
            toUpdate = true;
        }
        else if (longitude < endBounds.southwest.longitude) {
            longitude = endBounds.southwest.longitude;
            toUpdate = true;
        }

        if (toUpdate) {
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            theMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        double[] toRet = {latitude, longitude};
        return toRet;
    }

    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();

        //double[] newLL = updateCameraPosition(dragPosition.latitude, dragPosition.longitude);

        player.setLat(dragPosition.latitude);
        player.setLon(dragPosition.longitude);

        System.out.println("Woah duuuuude that's so ratchet");
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player playerN) {
        player = playerN;
    }

    @Override
    public void timerComplete() {
        //update position

        double latitude;
        double longitude;

        if (useGPS == true)
        {
            tracker = new GPSTracker(getApplicationContext());
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            double[] coords = fakeCoords(latitude, longitude);

            double[] newLL = updateCameraPosition(coords[0], coords[1]);

            player.setLat(newLL[0]);
            player.setLon(newLL[1]);

            pMark.setPosition(new LatLng(newLL[0], newLL[1]));

        }

        postman = new PostRequester(this, 0);       //#derp
        postman.newRequest("http://www.isaidso.ca/code/PeakMethods.php", 2);
        System.out.println(player.getLat() + ", " + player.getLon());
        postman.addParam("lat", "" + player.getLon());
        postman.addParam("lon", "" + player.getLat());
        postman.sendRequest();

        if( counter%6 == 0)
        {
            counter = 0;
            getAllClaims();
        }
        counter++;

        //start async task
        BackgroundUpdater bu = new BackgroundUpdater();
        bu.init(this);
    }

    private void getAllClaims(){
        postman = new PostRequester(this, 2);
        postman.newRequest("http://www.isaidso.ca/code/getAllClaims.php",1);
        postman.addParam("bull", "shit");
        postman.sendRequest();
    }

    public void claimUnclaimed(View v){
        postman = new PostRequester(this, 3);
        postman.newRequest("http://www.isaidso.ca/code/claimPeak.php",8);
        postman.addParam("lat", ""+player.getLat());
        postman.addParam("lon", ""+player.getLon());
        postman.addParam("xTile", ""+ (int) latToY(yDist(player.getLon())));
        postman.addParam("yTile", ""+ (int) lonToX(xDist(player.getLat())));
        postman.addParam("name", "Mt. Generic");
        postman.addParam("height", ""+curHeight);
        postman.addParam("ownername", ""+player.getUsername());
        postman.addParam("worth", ""+(curHeight/10));
        postman.sendRequest();
    }

    public void claimClaimed(View v){
        postman = new PostRequester(this, 4);
        postman.newRequest("http://www.isaidso.ca/code/claimPeak.php",8);
        postman.addParam("lat", ""+player.getLat());
        postman.addParam("lon", ""+player.getLon());
        postman.addParam("xTile", ""+ (int) latToY(yDist(player.getLon())));
        postman.addParam("yTile", ""+ (int) lonToX(xDist(player.getLat())));
        postman.addParam("name", "Mt. Generic");
        postman.addParam("height", ""+curHeight);
        postman.addParam("ownername", ""+player.getUsername());
        postman.addParam("worth", ""+(curHeight/10));
        postman.sendRequest();
    }

    private static double latToY(double latDist)
    {
        //convert Dist in lat. to Dist in pixels
        double pixelDiff = latDist/LAT_TO_PIXEL;



        return pixelDiff;
    }

    private static double lonToX(double lonDist)
    {
        //convert Dist in lon. to Dist in pixels
        double pixelDiff = lonDist/LON_TO_PIXEL;

        return pixelDiff;
    }

    //I think these are fairly clear
    //Some number between 0 and 0.25
    private static double yDist(double latPos)
    {
        double dist;

        dist = Math.abs(latPos - tLLat);

        if(dist > 0.5) dist = -1;		//there was an error

        return dist;
    }

    //Ditto . . . . . ^
    //Some number between 0 and 0.5
    private static double xDist(double lonPos)
    {
        double dist;

        dist = Math.abs(lonPos - tLLon);

        if(dist > 0.25) dist = -1;		//there was an error

        return dist;
    }

    @Override
    public void recievePostResults(String result, int channel) {
        if(channel == 0){
            // Peak Test
            String[] resultAr = result.split(",");
            if(resultAr[0].equals("TRUE")){
                startClaimCheck(player.getLat(), player.getLon());
                curHeight = Integer.parseInt(resultAr[1]);
            }else{
                Button fucker = (Button)findViewById(R.id.claimClaimed);
                fucker.setVisibility(View.GONE);
                Button shitHead = (Button)findViewById(R.id.claimUnclaimed);
                shitHead.setVisibility(View.GONE);
            }
        }else if(channel == 1){
            // Claim Test
            System.out.println(result);
            if(result.equals("success")){
                Button fucker = (Button)findViewById(R.id.claimClaimed);
                fucker.setVisibility(View.VISIBLE);
            }else{
                Button shitHead = (Button)findViewById(R.id.claimUnclaimed);
                shitHead.setVisibility(View.VISIBLE);
            }
        }else if (channel == 2){
            // Claims Get
            if(result.length() > 0){
                if(result.startsWith("DOLLAH")){
                    result = result.substring(6,result.length());
                    System.out.println(result);
                    String[] resultAr = result.split("#");
                    mapClaims = new Claim[resultAr.length];
                    for(int i=0; i<resultAr.length; i++){
                        //Array format: name,height,owner,lat,lon,worth
                        String[] claimAr = resultAr[i].split(",");
                        mapClaims[i] = new Claim();
                        mapClaims[i].setName(claimAr[0]);
                        mapClaims[i].setHeight(Integer.parseInt(claimAr[1]));
                        Player owner = new Player();
                        owner.setUsername(claimAr[2]);
                        mapClaims[i].setOwner(owner);
                        mapClaims[i].setLatitude(Double.parseDouble(claimAr[3]));
                        mapClaims[i].setLongitude(Double.parseDouble(claimAr[4]));
                        mapClaims[i].setWorth(Integer.parseInt(claimAr[5]));
                    }
                    resetClaims();
                }
            }
        }
    }

}
