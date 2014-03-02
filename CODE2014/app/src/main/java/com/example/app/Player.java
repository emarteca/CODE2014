package com.example.app;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

public class Player extends Person implements postListener {

    private Friend[] friends;
    protected double lon;
    protected double lat;
    private MainScreen activity;

    public void initData(int userid, MainScreen a) {
        //get Username
        activity = a;
        this.ourid = userid;

        //Request player data
        PostRequester pr = new PostRequester(this, 0);
        pr.newRequest("http://www.isaidso.ca/code/getPlayerData.php",1);
        pr.addParam("userid", "" + userid);
        pr.sendRequest();
    }

    public void initData(int userid) {
        //get Username
        this.ourid = userid;

        //Request player data
        PostRequester pr = new PostRequester(this, 0);
        pr.newRequest("http://www.isaidso.ca/code/getPlayerData.php",1);
        pr.addParam("userid", "" + userid);
        pr.sendRequest();
    }

    public void recievePostResults(String results, int channel){
        //Array: name,emptyFriends,color,claims,score
        System.out.println(results);
        String[] resultsAr = results.split(",");
        System.out.println(resultsAr.length);

        //get ID
        username = resultsAr[0];

        //get Friends
        //String[] friendsAr = resultsAr[1].split(",");
        //friends = new Friend[friendsAr.length];
        //Friend.getFriendsForPlayer(id,this);

        //get playerColor
        playerColor = Color.rgb(Integer.parseInt(resultsAr[2]),Integer.parseInt(resultsAr[3]),Integer.parseInt(resultsAr[4]));

        //get Claims
        String[] claimsAr = resultsAr[5].split(",");
        //claims = new Claim[claimsAr.length];
        //Claim.getClaimsForPlayer(ourid, this);
        //claimsOwned = claims.length;

        //get Score
        score = Integer.parseInt(resultsAr[6]);

        System.out.println(username + " " + playerColor + " " + score);

        if(activity != null){
            String test = username + " " + playerColor + " " + score;
            //activity.testPlayer(test);
        }
    }

    public Friend[] getFriends() {
        return friends;
    }

    public void setFriends(Friend[] friends) {
        this.friends = friends;
    }

    public static Person getPlayerByID(int id) {
        Player ownerPlayer = new Player();
        ownerPlayer.initData(id);
        return ownerPlayer;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
