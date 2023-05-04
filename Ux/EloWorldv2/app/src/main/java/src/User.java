package src;

import org.json.JSONObject;

public class User {

    String publicKey;

    public User(String publicKey) {this.publicKey = publicKey;}

    public String getPublicKey() {return publicKey;}
    
    //TODO fetch elo from DB leaderboard
    public int getELO() {return 0;}
}