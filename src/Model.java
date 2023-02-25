import java.util.List;
import java.util.Map;
import java.util.Objects;

import Enum.*;

/**
 * Inspired from the Projet d'année MainModel class (from Boris Petrov)
 */

public class Model extends Subject {
    private String username;
    private String memberSince;

    private int elo = Util.BASE_ELO;
    private int refereeScore;
    private List<String> friends;
    private List<Map.Entry<Integer, String>> leaderboard;
    private UserRoles role;
    private boolean isLoggedIn = false;

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


    public void setUp(String memberSince, List<String> friends, UserRoles role, int elo, int refereeScore) {
        setMemberSince(memberSince);
        setFriendList(friends);
        setRole(role);
        setELO(elo);
        setRefereeScore(refereeScore);

    }
    /**
     * Method called upon log out, clear all attributes
     */
    public void logsOut() {
        setUsername(null);
        setMemberSince(null);
        setELO(Util.BASE_ELO);
        setRefereeScore(0);
        clearFriends();
        isLoggedIn = false;
        setRole(UserRoles.NOT_LOGGED_IN);
    }

    public void addFriend(String friend) throws IllegalArgumentException {
        if (friends.contains(friend)) throw new IllegalArgumentException("Friend is already in friends");
        friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }

    public void setFriendList(List<String> newFriends) {friends = newFriends;}

    public void clearFriends() {friends.clear();}

    public int getRefereeScore() {
        return refereeScore;
    }

    public void setRefereeScore(int newScore) {
        refereeScore = newScore;
    }

    public int getELO() {
        return elo;
    }

    public void setELO(int newELO) {
        elo = newELO;
        notifyObservers();
    }

    public void setRole(UserRoles newRole) {
        role = newRole;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public UserRoles getRole() {
        return role;
    }

}

