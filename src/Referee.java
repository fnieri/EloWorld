import Exceptions.UserNotInEntry;
import org.json.JSONObject;
import java.time.LocalTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class Referee extends User implements Serializable {
    BlockChain blockchain;


    /**
     * Instantiation Constructor for Referee
     *
     * @param publicKey Username of the referee
     */
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

    /**
     * Create an entry to store in a local JSON file
     *
     * @param eloPlayer1 Updated elo of the first player of the match
     * @param eloPlayer2 Updated elo of the second player of the match
     * @param playerOneKey First player username
     * @param playerTwoKey Second player username
     * @return JSON String of the entry
     */
    public void createEntry(int eloPlayer1, String playerOneKey, int eloPlayer2, String playerTwoKey, String refereeKey) {
        //parsing dans Block.java
        JSONObject entry = new JSONObject();
        entry.put(JsonStrings.PLAYER_1_ELO, eloPlayer1);
        entry.put(JsonStrings.PLAYER_2_ELO, eloPlayer2);
        entry.put(JsonStrings.PLAYER_1_KEY, playerOneKey);
        entry.put(JsonStrings.PLAYER_2_KEY, playerTwoKey);
        entry.put(JsonStrings.REFEREE_KEY, refereeKey);
        entry.put(JsonStrings.REFEREE_SCORE, getRefereeScore());
        entry.put(JsonStrings.TIMESTAMP, LocalTime.now());
        Util.writeJSONFile(entry.toString(), Util.PATH_TO_ENTRIES_FOLDER);
    }

    /**
     * Format the leaderboard of the referee's blockchain into a JSONObject
     *
     * @return JSONObject of the blockchain's leaderboard
     * @throws UserNotInEntry security layer to ensure they are correctly parsed in their entries
     */
    public JSONObject getLeaderboard() throws UserNotInEntry {
        return blockchain.getLeaderboard();
    }

    /**
     * Get the score of the referee, it currently is the score of the blockchain but if needed could be calculated
     * in a more complex way.
     *
     * @return the score of the referee
     */
    public int getRefereeScore() {
        return blockchain.getScore();
    }

    /**
     * formats a JSONObject structure of the entire Blockchain, with as key the filename and as value the
     * file content, for all files composing the Blockchain.
     *
     * @return JSONObject of the Blockchain.
     */
    public JSONObject getBlockchain() {
        JSONObject jsonBlockchain = new JSONObject();

        File folder = new File(Util.PATH_TO_BLOCKCHAIN_FOLDER);

        for (final File fileEntry: Objects.requireNonNull(folder.listFiles())) {

            String filename = fileEntry.getName();
            jsonBlockchain.put(filename, Util.convertJsonFileToJSONObject(filename));
        }

        return jsonBlockchain;
    }

    public void setBlockchain(JSONObject newBlockchain) {
        for (Iterator<String> it = newBlockchain.keys(); it.hasNext(); ) {
            String key = it.next();
            Util.writeJSONFile(newBlockchain.getString(key), Util.PATH_TO_BLOCKCHAIN_FOLDER + key);
        }
    }

    /**
     * Returns all the data of the Referee as a JSONObject
     *
     * @return JSONObject of the referee
     */
    @Override
    public JSONObject asJson() {
        JSONObject refereeJson =  new JSONObject();
        refereeJson.put(getPublicKey(), getRefereeScore());
        return refereeJson;
    }

}
