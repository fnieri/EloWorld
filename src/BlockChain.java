import Exceptions.UserNotInEntry;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * Blockchain Class tasked to parse blocks from db/json and query data from it and add blocks to the blockchain
 * Author : Merian Emile
 * Date 19/02/2023
 */
public class BlockChain {
    Block lastBlock;
    String lastBlockData;

    public void Blockchain(String lastBlockData) {
        this.lastBlockData = lastBlockData;
        lastBlock = new Block(getLastBlockID());
    }
    public int getScore() {
        Block currBlock = lastBlock;
        int numBlock = 1;
        int numEntries = currBlock.getScore();
        // Data Count
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.blockHash)) {
            currBlock = new Block(currBlock.getPreviousBlockHash());
            numBlock += 1;
            numEntries += currBlock.getScore();
        }
        return numEntries/numBlock;
    }

    public int getELO(String userPublicKey) {
        Block currBlock = lastBlock;
        PlayerSearch playerELO = currBlock.getELO(userPublicKey);
        if (playerELO.found()) {return playerELO.ELO();}
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.blockHash)) {
            currBlock = new Block(currBlock.getPreviousBlockHash());
            playerELO = currBlock.getELO(userPublicKey);
            if (playerELO.found()) {return playerELO.ELO();}
        }
        return Util.BASE_ELO;
    }

    public Hashtable<String, Integer> getLeaderboard() throws UserNotInEntry {
        Block currBlock = lastBlock;
        Hashtable<String, Integer> leaderboard = new Hashtable<>();
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.blockHash)) {
            for (BlockEntry entry: currBlock.getEntries()) {
                String[] players = {entry.getPlayer1PublicKey(), entry.getPlayer2PublicKey()};
                for (String player: players) {
                    if (!leaderboard.containsKey(player)) {
                        leaderboard.put(player, entry.getPlayerELO(player));
                    }
                }
            }
            currBlock = new Block(currBlock.getPreviousBlockHash());

        }
        return leaderboard;
    }

    public void addBlock(List<BlockEntry> entries) {}

    public String getLastBlockID() {return new JSONObject(lastBlockData).getString(JsonStrings.LAST_BLOCK);}

}

