import Exceptions.UserNotInEntry;
import org.json.JSONObject;
import java.time.LocalTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Referee extends User implements Serializable {
    BlockChain blockchain;


    public Referee(String publicKey) {
        super(publicKey);
        String pathname = Util.PATH_TO_BLOCKCHAIN_FOLDER + Util.BLOCKCHAIN_HEAD + ".json";
        File f = new File(pathname);

        // blockchain directory already exists
        if(f.exists() && !f.isDirectory()) {blockchain = new BlockChain();}

        // create Blockchain directory
        else {
            JSONObject head = new JSONObject();
            head.put(JsonStrings.LAST_BLOCK, Util.FIRST_BLOCK);
            head.put(JsonStrings.BLOCK_NO, 1);
            try (FileWriter file = new FileWriter(pathname)) {
                file.write(head.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String createEntry(int eloPlayer1, int eloPlayer2, int playerOneKey, int playerTwoKey) {
        //parsing dans Block.java
        String newEntry = "";
        newEntry += eloPlayer1 + " ";
        newEntry += eloPlayer2 + " ";
        newEntry += this.getRefereeScore() + " ";
        newEntry += playerOneKey + " ";
        newEntry += playerTwoKey + " ";
        newEntry += LocalTime.now();
        return newEntry;
    }

    public JSONObject getLeaderboard() throws UserNotInEntry {
        return blockchain.getLeaderboard();
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
