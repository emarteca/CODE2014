package com.example.app;

public class Person {
    protected int ourid;
    protected String username;
    protected int score;
    protected int playerColor;
    protected int claimsOwned;
    protected Claim highestPeak;

    public Claim[] getClaims() {
        return claims;
    }

    public void setClaims(Claim[] claims) {
        this.claims = claims;
    }

    public int getId() {
        return ourid;
    }

    public void setId(int id) {
        this.ourid = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public int getClaimsOwned() {
        return claimsOwned;
    }

    public void setClaimsOwned(int claimsOwned) {
        this.claimsOwned = claimsOwned;
    }

    public Claim getHighestPeak() {
        return highestPeak;
    }

    public void setHighestPeak(Claim highestPeak) {
        this.highestPeak = highestPeak;
    }

    protected Claim[] claims;
}
