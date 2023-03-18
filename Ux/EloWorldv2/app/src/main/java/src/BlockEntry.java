package src;
/**
 * Class representing an entry on a block in the entry section
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

/**
 * Class representing an entry on a block in the entry section
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import androidx.annotation.NonNull;

import src.Exceptions.UserNotInEntry;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

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
     * Returns the ELO of a searched player, if the public key of said player is in the current entry
     *
     * @param playerPublicKey Public key of currently sought player
     * @throws UserNotInEntry If user is not in the current entry
     */
    public int getPlayerELO(String playerPublicKey) throws UserNotInEntry {
        if (Objects.equals(winner, playerPublicKey)) return player1ELO;
        else if (Objects.equals(loser, playerPublicKey)) return player2ELO;

        else {
            String exceptionMessage = "Key " + playerPublicKey + " not found for entry";
            throw new UserNotInEntry(exceptionMessage);
        }
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

    public String refereeKey() {
        return this.refereeKey;
    }

    public int refereeScore() {
        return this.refereeScore;
    }

    public String player1PublicKey() {
        return this.winner;
    }


    public String player2PublicKey() {
        return this.loser;
    }

    @NonNull
    public String toString() {
        return "Gagnant: " + winner + " / " + "Perdant" + loser;
    }
}