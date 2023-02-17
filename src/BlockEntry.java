/**
 * Class representing an entry on a block in the entry section
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import Exceptions.UserNotInEntry;
import java.util.Objects;

public class BlockEntry implements Serializable {
    int entryID;
    int timestamp;
    String parentBlockHash;
    String refereeKey;
    int refereeScore;
    String player1PublicKey;
    int player1ELO;
    String player2PublicKey;
    int player2ELO;

    public BlockEntry(int entryID,
                      int timestamp,
                      String parentBlockHash,
                      String refereeKey,
                      int refereeScore,
                      String player1PublicKey,
                      int player1ELO,
                      String player2PublicKey,
                      int player2ELO) {
        this.entryID = entryID;
        this.timestamp = timestamp;
        this.parentBlockHash = parentBlockHash;
        this.refereeKey = refereeKey;
        this.refereeScore = refereeScore;
        this.player1PublicKey = player1PublicKey;
        this.player1ELO = player1ELO;
        this.player2PublicKey = player2PublicKey;
        this.player2ELO = player2ELO;
    }

    /**
    * Returns the ELO of a searched player, if the public key of said player is in the current entry
    @param playerPublicKey Public key of currently sought player
    @throws UserNotInEntry If user is not in the current entry
    */
    public int getPlayerELO(String playerPublicKey) throws UserNotInEntry {
        if (Objects.equals(player1PublicKey, playerPublicKey)) return player1ELO;
        else if (Objects.equals(player2PublicKey, playerPublicKey)) return player2ELO;

        else {
            String exceptionMessage = "Key " + playerPublicKey + " not found for entry " + entryID + " of block " + parentBlockHash;
            throw new UserNotInEntry(exceptionMessage);
        }
    }

    @Override
    public void asJson() {}

    public String getRefereeKey() {
        return refereeKey;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getEntryID() {
        return entryID;
    }

    public int getEntryScore() {
        return refereeScore;
    }
}
