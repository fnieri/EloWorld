import org.json.JSONObject;

public class User {

    String username;
    String publicKey;
    int ELO;

    public String getPublicKey() {
        return publicKey;
    }

    // TODO cette fonction doit demander au serveur le elo du joueur par rapport au classement stocké dans le serveur.
    //  Si le serveur ne trouve pas ce joueur, on renvoie le elo par défaut.
    public int getELO() {return 0;}

    // TODO cette fonction envoie au superUser une requete de promotion
    public void requestPromotion() {

    }
}