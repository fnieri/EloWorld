import org.json.JSONObject;

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
        int numEntries = 0;
        int numBlock = 0;
        Block currBlock = lastBlock;
        // Data Count
        do {
            numBlock += 1;
            numEntries += currBlock.getScore();
            currBlock = new Block(currBlock.getPreviousBlockHash());
        } while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.blockHash));
        return numEntries/numBlock;
    }

    public int getELO(String playerName) {

    }

    public void getLeaderboard(String club) {}

    public String getLastBlockID() {return new JSONObject(lastBlockData).getString(JsonStrings.LAST_BLOCK);}

}

