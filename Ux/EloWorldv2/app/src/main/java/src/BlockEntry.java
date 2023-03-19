/**
 * Class representing an entry on a block in the entry section
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

package src;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlockEntry implements Serializable {

    private final String timestamp;
    private final String refereeKey;
    private final int refereeScore;
    private final String winner;
    private final String loser;


    public BlockEntry(String timestamp, String refereeKey, int refereeScore, String winner, String loser) {
        this.timestamp = timestamp;
        this.refereeKey = refereeKey;
        this.refereeScore = refereeScore;
        this.winner = winner;
        this.loser = loser;
    }

    public BlockEntry(JSONObject entry) throws JSONException {
        this.timestamp = entry.getString(JsonStrings.TIMESTAMP);
        this.refereeKey = entry.getString(JsonStrings.REFEREE_KEY);
        this.refereeScore = entry.getInt(JsonStrings.REFEREE_SCORE);
        this.winner = entry.getString(JsonStrings.WINNER);
        this.loser = entry.getString(JsonStrings.LOSER);
    }

    /**
     * @return BlockEntry as JSONObject
     */
    @Override
    public JSONObject asJson() throws JSONException {
        JSONObject serializedEntry = new JSONObject();
        serializedEntry.put(JsonStrings.TIMESTAMP, timestamp);
        serializedEntry.put(JsonStrings.REFEREE_KEY, refereeKey);
        serializedEntry.put(JsonStrings.REFEREE_SCORE, refereeScore);
        serializedEntry.put(JsonStrings.WINNER, winner);
        serializedEntry.put(JsonStrings.LOSER, loser);
        return serializedEntry;
    }

    public String timestamp() {
        return this.timestamp;
    }

    public ArrayList<String> getPlayers() {
        ArrayList<String> players = new ArrayList<>();
        players.add(this.winner);
        players.add(this.loser);
        return players;
    }

    public String getWinnerPublicKey() {
        return this.winner;
    }

    public String getLoserPublicKey() {
        return this.loser;
    }

    @NonNull
    public String toString() {
        return "Gagnant: " + winner + " / " + "Perdant: " + loser;
    }
}