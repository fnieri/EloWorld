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
    private boolean isReferee = false;
    private boolean isSuperUser = false;
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

    public void logsIn(String username, String role) throws IllegalArgumentException {
        isLoggedIn = true;
        setUsername(username);
        if (Objects.equals(role, UserRoles.USER.serialized())) {}
        else if (Objects.equals(role, UserRoles.REFEREE.serialized())) isReferee = true;
        else if (Objects.equals(role, UserRoles.SUPER_USER.serialized())) isSuperUser = true;
        else throw new IllegalArgumentException("Illegal role passed");
    }

    /**
     * Method called upon log out, clear all attributes
     */
    public void logsOut() {
        username = null;
        memberSince = null;
        elo = Util.BASE_ELO;
        refereeScore = 0;
        friends.clear();
        isLoggedIn = false;
        isReferee = false;
        isSuperUser = false;
    }

    public void addFriend(String friend) throws IllegalArgumentException {
        if (friends.contains(friend)) throw new IllegalArgumentException("Friend is already in friends");
        friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }
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
}

