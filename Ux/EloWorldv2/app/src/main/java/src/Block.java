package src; /**
 * Class representing a block in the blockchain
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import src.Exceptions.UserNotInEntry;
import src.Exceptions.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Comparator;

class Block implements Serializable {
    private final String blockHash;
    private final String previousBlockHash;
    private final List<BlockEntry> entries;
    private boolean hasData = false;
    private boolean hasEntries = false;
    private BlockParser parser = null;
    private final int score;

    /**
     * Constructor used when instantiating from a JSON file
     * @param data Json File
     */
    public Block(String data) throws JSONException {
        this.hasData = true;
        parser = new BlockParser(data);
        this.blockHash = parser.getBlockHash();
        this.previousBlockHash = parser.getPreviousBlockHash();
        this.entries = getEntriesFromData();
        this.score = entries.size();
    }

    /**
     * Constructor used for testing
     * @param blockHash Hash of current block
     * @param previousBlockHash Hash of parent
     * @param entries List of BlockEntry
     */
    public Block(String blockHash, String previousBlockHash, List<BlockEntry> entries) {
        this.blockHash = blockHash;
        this.previousBlockHash = previousBlockHash;
        this.entries = entries;
        this.hasEntries = true;
        this.score = entries.size();
    }

    /**
     * Sort block entries by their timestamp
     */
    private void sortEntriesByTimestamp() {
        entries.sort(Comparator.comparing(BlockEntry::timestamp));
    }

    /**
     * Return a list of blockEntries from block data
     */
    private List<BlockEntry> getEntriesFromData() throws UnsupportedOperationException, JSONException {
        if (!hasData) throw new UnsupportedOperationException("Entry creation from data called without loading data");
        hasEntries = true;
        return parser.getBlockEntries();
    }

    /**
     *
     * @return Block object as a JSONObject
     */
    @Override
    public JSONObject asJson() throws JSONException {
        JSONObject block = new JSONObject();

        block.put(JsonStrings.BLOCK_HASH, blockHash);
        block.put(JsonStrings.PARENT_BLOCK_HASH, previousBlockHash);
        JSONArray entriesArray = entriesAsJson();
        block.put(JsonStrings.ENTRIES, entriesArray);
        return block;
    }

    /**
     * Return a JSONArray containing all entries as JSON
     * @return Entries as a JSONArray
     * @throws UnsupportedOperationException If no entries are found
     */
    private JSONArray entriesAsJson() throws UnsupportedOperationException, JSONException {
        if (entries.size() == 0) throw new UnsupportedOperationException("Cannot convert empty entry list to JSON");

        JSONArray jsonArray = new JSONArray();
        int entryCounter = 0;

        for (BlockEntry entry: entries)  {
            JSONObject jsonEntry = entry.asJson();
            jsonEntry.put(JsonStrings.ENTRY, entryCounter);
            jsonArray.put(jsonEntry);
            entryCounter += 1;
        }
        return jsonArray;
    }

    /**
     * Find ELO of a player on the blocks entries using its public key
     * @param userPublicKey Public key of a Player
     * @return PlayerSearch : record indicating whether the Player was found among all entries along with its ELO, if the player was not found, return previousBlocksID
     *
     */
    public PlayerSearch getELO(String userPublicKey) {
        for (BlockEntry entry: entries) {
            try {
                int userELO = entry.getPlayerELO(userPublicKey);
                return new PlayerSearch(true, userELO, "");
            }
            catch (UserNotInEntry ignored) {
            }
        }
        return new PlayerSearch(false, 0, previousBlockHash);
    }

    public int getScore() {
        return score;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public List<BlockEntry> getEntries() {return this.entries;}

    public String getHash() throws Exception {
        SHA256 hasher = SHA256.getInstance();
        return hasher.computeHash(asJson().toString());
    }
}