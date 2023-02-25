import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class BlockChainTest {

    @Test
    void blockchain() {
        String headPointer = Util.PATH_TO_BLOCKCHAIN_FOLDER + "HEAD.json";
        BlockChain blockchain = new BlockChain(headPointer);
        assertEquals(2, blockchain.blockCount);
        assertEquals("Block2", blockchain.lastBlock.getBlockHash());
        assertEquals("Block1", blockchain.lastBlock.getPreviousBlockHash());
    }
    @Test
    void getScore() {
        String headPointer = Util.PATH_TO_BLOCKCHAIN_FOLDER + "HEAD.json";
        BlockChain blockchain = new BlockChain(headPointer);

        assertEquals(150, blockchain.getELO("theo12"));
        assertEquals(140, blockchain.getELO("emile11"));

    }

    @Test
    void getELO() {
    }

    @Test
    void getLeaderboard() {
    }

    @Test
    void addBlock() {
    }

    @Test
    void getLastBlockID() {
    }
}