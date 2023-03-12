package src; /**
 * Class that parses data from a block to create a list of block entries
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlockParser {

    private JSONObject jsonObject;
    private final String data;

    public BlockParser(String data) {
        this.data = data;
        loadJson();
    }

    private void loadJson() {
        try {
            jsonObject = new JSONObject(data);
        }catch (JSONException err){
            throw new IllegalArgumentException("Wrong json file format");
        }
    }


    /**
     * Generate a list of blockEntries from a JSON file
     *
     * @return A list of block entries
     * @throws IllegalArgumentException If the json file is wrongly formatted
     */
    public List<BlockEntry> getBlockEntries() throws IllegalArgumentException, JSONException {

        List<BlockEntry> blockEntries = new ArrayList<>();

        JSONArray entries = getJsonEntries();
        for (int i = 0; i < entries.length(); i++) {
            JSONObject entry = entries.getJSONObject(i);
            blockEntries.add(getEntryFromJsonObject(entry));
        }
        return blockEntries;
    }

    /**
     * Create a blockEntry object from a given entry of a jsonArray
     * @param entry JSONArray entry
     * @return Block entry containing the parameters of a JSONObject
     */
    public BlockEntry getEntryFromJsonObject(JSONObject entry) throws JSONException {
        return new BlockEntry(
                entry.getInt(JsonStrings.TIMESTAMP),
                entry.getString(JsonStrings.REFEREE_KEY),
                entry.getInt(JsonStrings.REFEREE_SCORE),
                entry.getString(JsonStrings.PLAYER_1_KEY),
                entry.getInt(JsonStrings.PLAYER_1_ELO),
                entry.getString(JsonStrings.PLAYER_2_KEY),
                entry.getInt(JsonStrings.PLAYER_2_ELO)
        );
    }

    /**
     * Get Hash of current block from json data
     * @return  Hash of current block
     */
    public String getBlockHash() throws JSONException {
        return jsonObject.getString(JsonStrings.BLOCK_HASH);
    }

    /**
     * Get Hash of previous block from json data
     * @return  Hash of previous block
     */
    public String getPreviousBlockHash() throws JSONException {
        return jsonObject.getString(JsonStrings.PARENT_BLOCK_HASH);
    }

    /**
     * Return an array of entries from a block's JSONObject
     * @return Array of json entries
     */
    public JSONArray getJsonEntries() throws JSONException {return jsonObject.getJSONArray(JsonStrings.ENTRIES);}
}
