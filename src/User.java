import org.json.JSONObject;

public class User {

    String username;
    String publicKey;
    int ELO;

    void register(){}

    void logIn(){}

    void calculateElo(){}

    public String getPublicKey() {
        return publicKey;
    }

    public int getELO() {
        return ELO;
    }
}