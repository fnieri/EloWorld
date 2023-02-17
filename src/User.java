public class User {

    String publicKey;
    int ELO;
    String id;
    String username;
    String password; // ca va falloir voir a le crypter

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