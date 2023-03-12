/**
 * Class representing an entry on a block in the entry section
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import Exceptions.UserNotInEntry;
import org.json.JSONObject;
import java.util.Objects;

public class BlockEntry implements Serializable {

    private final int timestamp;
    private final String refereeKey;
    private final int refereeScore;
    private final String player1PublicKey;
    private final int player1ELO;
    private final String player2PublicKey;
    private final int player2ELO;

    public BlockEntry(int timestamp, String refereeKey, int refereeScore, String player1PublicKey, int player1ELO, String player2PublicKey, int player2ELO) {
        this.timestamp = timestamp;
        this.refereeKey = refereeKey;
        this.refereeScore = refereeScore;
        this.player1PublicKey = player1PublicKey;
        this.player1ELO = player1ELO;
        this.player2PublicKey = player2PublicKey;
        this.player2ELO = player2ELO;
    }

    public BlockEntry(JSONObject entry) {
        this.timestamp = entry.getInt(JsonStrings.TIMESTAMP);
        this.refereeKey = entry.getString(JsonStrings.REFEREE_KEY);
        this.refereeScore = entry.getInt(JsonStrings.REFEREE_SCORE);
        this.player1PublicKey = entry.getString(JsonStrings.PLAYER_1_KEY);
        this.player1ELO = entry.getInt(JsonStrings.PLAYER_1_ELO);
        this.player2PublicKey = entry.getString(JsonStrings.PLAYER_2_KEY);
        this.player2ELO = entry.getInt(JsonStrings.PLAYER_2_ELO);
    }

    /**
     * Returns the ELO of a searched player, if the public key of said player is in the current entry
     *
     * @param playerPublicKey Public key of currently sought player
     * @throws UserNotInEntry If user is not in the current entry
     */
    public int getPlayerELO(String playerPublicKey) throws UserNotInEntry {
        if (Objects.equals(player1PublicKey, playerPublicKey)) return player1ELO;
        else if (Objects.equals(player2PublicKey, playerPublicKey)) return player2ELO;

        else {
            String exceptionMessage = "Key " + playerPublicKey + " not found for entry";
            throw new UserNotInEntry(exceptionMessage);
        }
    }

    /**
     * @return BlockEntry as JSONObject
     */
    @Override
    public JSONObject asJson() {
        JSONObject serializedEntry = new JSONObject();
        serializedEntry.put(JsonStrings.TIMESTAMP, timestamp);
        serializedEntry.put(JsonStrings.REFEREE_KEY, refereeKey);
        serializedEntry.put(JsonStrings.REFEREE_SCORE, refereeScore);
        serializedEntry.put(JsonStrings.PLAYER_1_KEY, player1PublicKey);
        serializedEntry.put(JsonStrings.PLAYER_1_ELO, player1ELO);
        serializedEntry.put(JsonStrings.PLAYER_2_KEY, player2PublicKey);
        serializedEntry.put(JsonStrings.PLAYER_2_ELO, player2ELO);
        return serializedEntry;
    }

    public int timestamp() {
        return this.timestamp;
    }

    public String refereeKey() {
        return this.refereeKey;
    }

    public int refereeScore() {
        return this.refereeScore;
    }

    public String player1PublicKey() {
        return this.player1PublicKey;
    }

    public int player1ELO() {
        return this.player1ELO;
    }

    public String player2PublicKey() {
        return this.player2PublicKey;
    }

    public int player2ELO() {
        return this.player2ELO;
    }

    public String toString() {
        return player1PublicKey + " - " + player1ELO + " / " + player2PublicKey + " - " + player2ELO;
    }
}
