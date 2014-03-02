package com.example.app;

public class Claim implements postListener{
    private int height;
    private Person owner;
    private String name;
    private int worth;
    private double longitude;
    private double latitude;
    private ownerTimer timer = new ownerTimer();
    private static Player playerTarget;

    public static void getClaimsForPlayer(int id, Player player) {
        //Request claim data
        playerTarget = player;
        PostRequester pr = new PostRequester(new Claim(),1);
        pr.newRequest("http://www.isaidso.ca/code/getClaimsForPlayer.php",1);
        pr.addParam("userid", "" + id);
        pr.sendRequest();
    }

    public static Claim getClaimByName(String s) {
        return null;
    }

    @Override
    public void recievePostResults(String result, int channel) {
        String[] postClaims = result.split("|");
        Claim[] claimList = new Claim[postClaims.length];
        for(int i=0; i<postClaims.length; i++){
            claimList[i] = makeClaimFromArray(postClaims[i].split(","));
        }
        playerTarget.setClaims(claimList);
    }

    private Claim makeClaimFromArray(String[] claimAr) {
        //Array format: name,height,ownerid,lat,lon,ownershipStart,worth
        if(claimAr.length != 0){
            Claim newClaim = new Claim();
            newClaim.name = claimAr[0];
            newClaim.height = Integer.parseInt(claimAr[1]);
            newClaim.owner = Player.getPlayerByID(Integer.parseInt(claimAr[2]));
            newClaim.latitude = Double.parseDouble(claimAr[3]);
            newClaim.longitude = Double.parseDouble(claimAr[4]);
            newClaim.timer.setStartTime(claimAr[5]);
            newClaim.worth = Integer.parseInt(claimAr[6]);
            return newClaim;
        }else{
            return null;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ownerTimer getTimer() {
        return timer;
    }

    public void setTimer(ownerTimer timer) {
        this.timer = timer;
    }
}
