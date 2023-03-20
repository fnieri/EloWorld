package src;
import android.webkit.MimeTypeMap;

import src.Exceptions.UserNotInEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import src.Enum.Domain;
import src.Exceptions.UserNotInEntry;

public class Referee extends User implements Serializable {
    BlockChain blockchain;
    ArrayList<BlockEntry> entries = new ArrayList<>();
    Util util = Util.getInstance();

    /**
     * Instantiation Constructor for Referee
     *
     * @param publicKey Username of the referee
     */
    public Referee(String publicKey) throws JSONException, FileNotFoundException {
        super(publicKey);
        String blockchainPath = util.getPathToBlockChain();
        File blockchainFile = new File(blockchainPath); //+ Util.BLOCKCHAIN_HEAD);
        if (!blockchainFile.exists() && blockchainFile.isDirectory()) {
            // create Blockchain directory
            blockchainFile.mkdirs();
            // Head file
            JSONObject head = new JSONObject();
            head.put(JsonStrings.LAST_BLOCK, util.FIRST_BLOCK);
            head.put(JsonStrings.BLOCK_NO, 1);
            util.writeJSONFile(head.toString(), blockchainPath + "/" + util.BLOCKCHAIN_HEAD + util.SUFFIX);

            // first block
            JSONObject firstBlock = new JSONObject();
            firstBlock.put(JsonStrings.BLOCK_HASH, util.FIRST_BLOCK);
            firstBlock.put(JsonStrings.PARENT_BLOCK_HASH, util.FIRST_BLOCK);
            firstBlock.put(JsonStrings.TIMESTAMP, LocalTime.now());
            JSONArray fakeEntriesArray = new JSONArray();
            JSONObject fakeEntry = new BlockEntry("", "", 0, "", "").asJson();
            fakeEntriesArray.put(fakeEntry);
            firstBlock.put(JsonStrings.ENTRIES, fakeEntriesArray);
            util.writeJSONFile(firstBlock.toString(), blockchainPath + "/" + util.FIRST_BLOCK + util.SUFFIX);
        }
        this.blockchain = new BlockChain();

        String entriesPath = util.PATH_TO_BLOCKCHAIN_FOLDER + File.separator + util.PATH_TO_ENTRIES_FOLDER;
        File entriesFolder = new File(entriesPath);
        entriesFolder.mkdirs();

        // read entries
        if (entriesFolder.exists() && entriesFolder.isDirectory()) {
            for (final File fileEntry : Objects.requireNonNull(entriesFolder.listFiles())) {
                String path = "entries" + File.separator + fileEntry.getName();
                JSONObject jsonEntry = util.convertJsonFileToJSONObject(path);
                BlockEntry entry = new BlockEntry(jsonEntry);
                this.entries.add(entry);
            }
        }
        // create entries directory
        else {
            try {
                Path path = Paths.get(util.PATH_TO_ENTRIES_FOLDER);
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Failed to create directory" + e.getMessage());
            }
        }
    }

    /**
     * Create an entry to store in a local JSON file
     *
     * @param winner   The winner's username
     * @param loser The loser's username
     * @param refereeKey The referee's public key
     */
    public void createEntry(String winner, String loser, String refereeKey) throws JSONException, FileNotFoundException {
        JSONObject entry = new JSONObject();

        entry.put(JsonStrings.WINNER, winner);
        entry.put(JsonStrings.LOSER, loser);
        entry.put(JsonStrings.REFEREE_KEY, refereeKey);
        entry.put(JsonStrings.REFEREE_SCORE, getRefereeScore());
        entry.put(JsonStrings.TIMESTAMP, String.valueOf(LocalTime.now()));
        String path = util.PATH_TO_ENTRIES_FOLDER + "Entry" + entries.size() + util.SUFFIX;
        String firstPath = util.getPathToBlockChain() + "/" + path ;
        util.writeJSONFile(entry.toString(), firstPath);
        this.entries.add(new BlockEntry(util.convertJsonFileToJSONObject(path)));
    }

    /**
     * Format the leaderboard of the referee's blockchain into a JSONObject
     *
     * @return JSONObject of the blockchain's leaderboard
     * @throws UserNotInEntry security layer to ensure they are correctly parsed in their entries
     */
    public JSONObject getLeaderboard() throws Exception {
        return blockchain.getLeaderboard();
    }

    /**
     * Get the score of the referee, it currently is the score of the blockchain but if needed could be calculated
     * in a more complex way.
     *
     * @return the score of the referee
     */
    public int getRefereeScore() throws JSONException {
        return blockchain.getScore();
    }

    public Block getBlock(int index) throws Exception {
        return this.blockchain.getBlock(index);
    }

    /**
     * formats a JSONObject structure of the entire Blockchain, with as key the filename and as value the
     * file content, for all files composing the Blockchain.
     *
     * @return JSONObject of the Blockchain.
     */
    public JSONObject getBlockchain() throws JSONException {
        JSONObject jsonBlockchain = new JSONObject();
        File folder = new File(util.getPathToBlockChain());
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                String fileExt = MimeTypeMap.getFileExtensionFromUrl(fileEntry.toString());
                if (Objects.equals(fileExt, "json")) {
                    String filename = fileEntry.getName();
                    jsonBlockchain.put(filename, util.convertJsonFileToJSONObject(filename));
                }
            }
        }
        return jsonBlockchain;
    }

    public BlockChain getBlockchainObject() {
        return this.blockchain;
    }

    public void setBlockchain(JSONObject newBlockchain) throws JSONException, IOException {

        // delete current blockchain
        File folder = new File(util.getPathToBlockChain());

        for(File file: Objects.requireNonNull(folder.listFiles())) {
            if (!file.isDirectory()) file.delete();

        }

        // replace with newer chosen blockchain
        JSONObject blockchain = new JSONObject(newBlockchain.getJSONObject(MessageStrings.BLOCKCHAIN_OBJECT).getString(MessageStrings.BLOCKCHAIN_OBJECT));

        for (Iterator<String> it = blockchain.keys(); it.hasNext(); ) {
            String key = it.next();

            util.writeJSONFile(blockchain.getString(key), util.getPathToBlockChain() + File.separator + key);
        }

        this.blockchain = new BlockChain();
    }

    /**
     * add a new block to the blockchain from the saved entries
     */
    public void addBlock() throws JSONException {
        String path = util.getPathToBlockChain() + File.separator + util.PATH_TO_ENTRIES_FOLDER;

        File folder = new File(path);
        this.blockchain.addBlock(this.entries);

        for(File file: Objects.requireNonNull(folder.listFiles())) {
            if (!file.isDirectory()) file.delete();
        }

        this.entries = new ArrayList<>();
    };

    /**
     * Returns all the data of the Referee as a JSONObject
     *
     * @return JSONObject of the referee
     */
    @Override
    public JSONObject asJson() throws JSONException {
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

