import Exceptions.UserNotInEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import Enum.Domain;

public class Referee extends User implements Serializable {
    BlockChain blockchain;
    ArrayList<BlockEntry> entries = new ArrayList<>();


    /**
     * Instantiation Constructor for Referee
     *
     * @param publicKey Username of the referee
     */
    public Referee(String publicKey) {
        super(publicKey);
        String blockchainPath = Util.PATH_TO_BLOCKCHAIN_FOLDER;
        File blockchainFile = new File(blockchainPath + Util.BLOCKCHAIN_HEAD + Util.SUFFIX);

        // create Blockchain directory
        if (!blockchainFile.exists() || blockchainFile.isDirectory()) {

            // Head file
            JSONObject head = new JSONObject();
            head.put(JsonStrings.LAST_BLOCK, Util.FIRST_BLOCK);
            head.put(JsonStrings.BLOCK_NO, 1);
            Util.writeJSONFile(head.toString(), blockchainPath + Util.BLOCKCHAIN_HEAD + Util.SUFFIX);

            // first block
            JSONObject firstBlock = new JSONObject();
            firstBlock.put(JsonStrings.BLOCK_HASH, Util.FIRST_BLOCK);
            firstBlock.put(JsonStrings.PARENT_BLOCK_HASH, Util.FIRST_BLOCK);
            firstBlock.put(JsonStrings.TIMESTAMP, LocalTime.now());
            firstBlock.put(JsonStrings.ENTRIES, new JSONArray());
            Util.writeJSONFile(firstBlock.toString(), blockchainPath + Util.FIRST_BLOCK + Util.SUFFIX);
        }
        this.blockchain = new BlockChain();

        String entriesPath = Util.PATH_TO_ENTRIES_FOLDER;
        File entriesFolder = new File(entriesPath);

        // read entries
        if (entriesFolder.exists() && entriesFolder.isDirectory()) {
            for (final File fileEntry : Objects.requireNonNull(entriesFolder.listFiles())) {
                JSONObject jsonEntry = Util.convertJsonFileToJSONObject(fileEntry.getPath());
                BlockEntry entry = new BlockEntry(jsonEntry);
                this.entries.add(entry);
            }
        }
        // create entries directory
        else {
            try {
                Path path = Paths.get(Util.PATH_TO_ENTRIES_FOLDER);
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Failed to create directory" + e.getMessage());
            }
        }
    }

    /**
     * Create an entry to store in a local JSON file
     *
     * @param eloPlayer1   Updated elo of the first player of the match
     * @param eloPlayer2   Updated elo of the second player of the match
     * @param playerOneKey First player username
     * @param playerTwoKey Second player username
     */
    public void createEntry(int eloPlayer1, String playerOneKey, int eloPlayer2, String playerTwoKey, String refereeKey) {
        JSONObject entry = new JSONObject();
        entry.put(JsonStrings.PLAYER_1_ELO, eloPlayer1);
        entry.put(JsonStrings.PLAYER_2_ELO, eloPlayer2);
        entry.put(JsonStrings.PLAYER_1_KEY, playerOneKey);
        entry.put(JsonStrings.PLAYER_2_KEY, playerTwoKey);
        entry.put(JsonStrings.REFEREE_KEY, refereeKey);
        entry.put(JsonStrings.REFEREE_SCORE, getRefereeScore());
        entry.put(JsonStrings.TIMESTAMP, LocalTime.now());
        String path = Util.PATH_TO_ENTRIES_FOLDER + "Entry" + entries.size() + Util.SUFFIX;
        Util.writeJSONFile(entry.toString(), path);
        this.entries.add(new BlockEntry(Util.convertJsonFileToJSONObject(path)));
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

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {

            String filename = fileEntry.getName();
            jsonBlockchain.put(filename, Util.convertJsonFileToJSONObject(filename));
        }


        return jsonBlockchain;
    }

    public void setBlockchain(JSONObject newBlockchain) {

        // delete current blockchain
        File folder = new File(Util.PATH_TO_BLOCKCHAIN_FOLDER);
        for(File file: Objects.requireNonNull(folder.listFiles())) {
            file.delete();
        }

        // replace with newer chosen blockchain
        for (Iterator<String> it = newBlockchain.keys(); it.hasNext(); ) {
            String key = it.next();
            Util.writeJSONFile(newBlockchain.getString(key), Util.PATH_TO_BLOCKCHAIN_FOLDER + key);
        }

        this.blockchain = new BlockChain();
    }

    /**
     * add a new block to the blockchain from the saved entries
     */
    public void addBlock() {
        this.blockchain.addBlock(this.entries);

        File folder = new File(Util.PATH_TO_ENTRIES_FOLDER);
        for(File file: Objects.requireNonNull(folder.listFiles())) {
            file.delete();
        }
    };

    /**
     * Returns all the data of the Referee as a JSONObject
     *
     * @return JSONObject of the referee
     */
    @Override
    public JSONObject asJson() {
        JSONObject refereeJson = new JSONObject();
        refereeJson.put(getPublicKey(), getRefereeScore());
        return refereeJson;
    }

    /**
     * Return the currently saved entries
     * @return list of pending entries
     */
    public ArrayList<BlockEntry> getEntries() {
        return entries;
    }
}
