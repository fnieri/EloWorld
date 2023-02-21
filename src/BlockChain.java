import Exceptions.UserNotInEntry;
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
        Block currBlock = lastBlock;
        int numBlock = 1;
        int numEntries = currBlock.getScore();
        // Data Count
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
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
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
            currBlock = new Block(currBlock.getPreviousBlockHash());
            playerELO = currBlock.getELO(userPublicKey);
            if (playerELO.found()) {return playerELO.ELO();}
        }
        return Util.BASE_ELO;
    }

    public void getLeaderboard(String club) {}

    public String getLastBlockID() {return new JSONObject(lastBlockData).getString(JsonStrings.LAST_BLOCK);}

}

