import org.json.JSONObject;

public class Referee extends User implements Serializable {

    int score;
    BlockChain blockchain;
    
    //TODO Constructeur
    public Referee() {}

    public String createEntry(int eloPlayer1, int eloPlayer2, int playerOneKey, int playerTwoKey) {
        //parsing dans Block.java
        String newEntry = "";
        newEntry += eloPlayer1 + " ";
        newEntry += eloPlayer2 + " ";
        newEntry += this.getScore() + " ";
        newEntry += playerOneKey + " ";
        newEntry += playerTwoKey + " ";
        newEntry += LocalTime.now();
        return newEntry;
    }

    public int getRefereeScore() {
        return blockchain.getScore();
    }

    @Override
    public JSONObject asJson() {
        JSONObject refereeJson =  new JSONObject();
        refereeJson.put(getPublicKey(), getRefereeScore());
        return refereeJson;
    }

}
