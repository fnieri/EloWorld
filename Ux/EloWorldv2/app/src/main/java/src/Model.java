package src;

import android.content.Context;
import android.icu.lang.UScript;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import src.Enum.*;
import src.Enum.UserRoles;

/**
 * Inspired from the Projet d'année MainModel class (from Boris Petrov)
 */

public class Model extends Subject {
    private String username;
    private String memberSince;

    private String publicKey;
    private String privateKey;

    private int refereeScore;
    private List<String> friends;
    //Leaderboard is an list of entries of type <Position, <Username, ELO>>
    private List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard;
    private UserRoles role;

    private boolean isLoggedIn = false;
    private boolean isSetUp = false;

    Referee referee;
    Util util = Util.getInstance();
    private int elo = util.BASE_ELO;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public void logsIn(String username) throws IllegalArgumentException {
        isLoggedIn = true;
        setUsername(username);
    }


    public void setUp(String memberSince, List<String> friends, UserRoles role, int elo, int refereeScore, String publicKey, String privateKey, List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard) throws JSONException, FileNotFoundException {
        setMemberSince(memberSince);
        setFriendList(friends);
        setRole(role);
        setELO(elo);
        setReferee();
        setRefereeScore(refereeScore);
        setPublicKey(publicKey);
        setPrivateKey(privateKey);
        setLeaderboard(leaderboard);
        isLoggedIn = true;
        isSetUp = true;
    }
    /**
     * Method called upon log out, clear all attributes
     */
    public void logsOut() {
        System.out.println("was oaessirt");
        setUsername(null);
        setMemberSince(null);
        setELO(util.BASE_ELO);
        setRefereeScore(0);
        referee = null;
        clearFriends();
        isLoggedIn = false;
        isSetUp = false;
        setRole(UserRoles.NOT_LOGGED_IN);
    }

    public Referee getReferee() throws  JSONException {
        if (getRole() == UserRoles.REFEREE) return referee;
        return null;
    }

    public void setReferee() throws JSONException, FileNotFoundException {
        if (getRole() == UserRoles.REFEREE) referee = new Referee(publicKey);
    }

    public void addFriend(String friend) {
        if (!friends.contains(friend)) friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }
    public List<String> getFriends() {return friends;}
    public void setFriendList(List<String> newFriends) {friends = newFriends;}

    public void clearFriends() {friends.clear();}

    public int getRefereeScore() throws JSONException {
        if (role == UserRoles.REFEREE) return referee.getRefereeScore();
        else return 0;
    }

    public void setRefereeScore(int newScore) {
        refereeScore = newScore;
    }

    public String getPublicKey() {return publicKey;}
    public void setPublicKey(String newPublicKey) {publicKey = newPublicKey;}

    public String getPrivateKey() {return privateKey;}
    public void setPrivateKey(String newPrivateKey) {privateKey = newPrivateKey;}

    public int getELO() {
        return elo;
    }

    public void setELO(int newELO) {
        elo = newELO;
        notifyObservers();
    }

    public List<Map.Entry<Integer, Map.Entry<String, Integer>>> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<Map.Entry<Integer, Map.Entry<String, Integer>>> newLeaderboard) {
        leaderboard = newLeaderboard;
    }


    public void setRole(UserRoles newRole) {
        role = newRole;
    }
    public void createEntry(String winner, String loser) throws JSONException, FileNotFoundException {
        if (role == UserRoles.REFEREE) referee.createEntry(winner, loser, getPublicKey());
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public boolean isSetUp() {return isSetUp;}
    public UserRoles getRole() {
        return role;
    }

}

