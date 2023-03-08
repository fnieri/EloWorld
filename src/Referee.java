import Exceptions.UserNotInEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        JSONArray filesArray = new JSONArray();

        File folder = new File(Util.PATH_TO_BLOCKCHAIN_FOLDER);

        for (final File fileEntry: Objects.requireNonNull(folder.listFiles())) {
            String filename = fileEntry.getName();
            String fileContent = Util.convertJsonFileToString(filename);
        }

        return jsonBlockchain;
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
