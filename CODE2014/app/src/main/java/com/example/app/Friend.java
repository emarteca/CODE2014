package com.example.app;

public class Friend extends Person implements postListener{
    private static Player playerTarget;

    public static void getFriendsForPlayer(int id, Player player) {
        playerTarget = player;

        //Request friend data
        PostRequester pr = new PostRequester(new Friend(),0);
        pr.newRequest("http://www.isaidso.ca/code/getFriendsForPlayer.php",1);
        pr.addParam("userid",id+"");
        pr.sendRequest();
    }

    @Override
    public void recievePostResults(String result,int channel) {
        String[] postFriends = result.split("|");
        Friend[] friendList = new Friend[postFriends.length];
        for(int i=0; i<postFriends.length; i++){
            friendList[i] = makeFriendFromArray(postFriends[i].split(","));
        }
        playerTarget.setFriends(friendList);
    }

    private Friend makeFriendFromArray(String[] friendAr) {
        Friend newFriend = new Friend();
        //Array format: id,username,score,color,highest
        newFriend.ourid = Integer.parseInt(friendAr[0]);
        newFriend.username = friendAr[1];
        newFriend.score = Integer.parseInt(friendAr[2]);
        newFriend.playerColor = Integer.parseInt(friendAr[3]);

        newFriend.highestPeak = Claim.getClaimByName(friendAr[4]);
        return newFriend;
    }
}
