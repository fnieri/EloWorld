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
    private final String winnerPublicKey;
    private final String loserPublicKey;

    public BlockEntry(String timestamp, String refereeKey, int refereeScore, String winner, String winnerPublicKey, String loser, String loserPublicKey) {
        this.timestamp = timestamp;
        this.refereeKey = refereeKey;
        this.refereeScore = refereeScore;
        this.winner = winner;
        this.loser = loser;
        this.winnerPublicKey = winnerPublicKey;
        this.loserPublicKey = loserPublicKey;
    }

    public BlockEntry(JSONObject entry) throws JSONException {
        this.timestamp = entry.getString(JsonStrings.TIMESTAMP);
        this.refereeKey = entry.getString(JsonStrings.REFEREE_KEY);
        this.refereeScore = entry.getInt(JsonStrings.REFEREE_SCORE);
        this.winner = entry.getString(JsonStrings.WINNER);
        this.loser = entry.getString(JsonStrings.LOSER);
        this.winnerPublicKey = entry.getString(JsonStrings.WINNER_KEY);
        this.loserPublicKey = entry.getString(JsonStrings.LOSER_KEY);
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
        serializedEntry.put(JsonStrings.WINNER_KEY, winnerPublicKey);
        serializedEntry.put(JsonStrings.LOSER_KEY, loserPublicKey);
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

    public String getWinner() {
        return this.winner;
    }
    public String getLoser() {
        return this.loser;
    }

    public String getWinnerPublicKey() {
        return this.winnerPublicKey;
    }

    public String getLoserPublicKey() {
        return this.loserPublicKey;
    }

    @NonNull
    public String toString() {
        return "Gagnant: " + winner + " / " + "Perdant: " + loser;
    }

    public String getBlockString() {
        return  "Gagnant: " + winner + "\n"
            +   "Clé gagnant: " + winnerPublicKey + "\n"
            +   "Perdant: " + loser + "\n"
            +   "Clé perdant: " + loserPublicKey + "\n"
            +   "Clé arbitre: " + refereeKey;
    }
}