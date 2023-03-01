import Exceptions.UserNotInEntry;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BlockChainTest {
    @Test
    void blockchain() {
        BlockChain blockchain = new BlockChain();

        assertEquals(3, blockchain.blockCount);
    }
    @Test
    void getScore() {
        BlockChain blockchain = new BlockChain();

        assertEquals(2, blockchain.getScore());
    }

    @Test
    void getELO() {
        BlockChain blockchain = new BlockChain();

        assertEquals(150, blockchain.getELO("theo12"));
        assertEquals(140, blockchain.getELO("emile11"));
    }

    @Test
    void getLeaderboard() throws UserNotInEntry {
        BlockChain blockchain = new BlockChain();

        JSONObject leaderboard = blockchain.getLeaderboard();
        assertFalse(leaderboard.has("fran11"));
        assertTrue(leaderboard.has("emile11"));
        assertEquals(140, leaderboard.getInt("emile12"));
        assertEquals(150, leaderboard.getInt("theo11"));
    }

    @Test
    void addBlock() {
        BlockChain blockchain = new BlockChain();
        ArrayList<BlockEntry> entries = new ArrayList<>();
        entries.add(new BlockEntry(4, "fran31", 1, "emile31", 160, "theo31", 149));
        entries.add(new BlockEntry(6, "elliot32", 1, "emile32", 130, "theo32", 189));

        blockchain.addBlock(entries);

        assertEquals("Block3", blockchain.lastBlock.getBlockHash());
        assertEquals("Block2", blockchain.lastBlock.getPreviousBlockHash());
        assertEquals(149, blockchain.getELO("theo31"));
        getELO();
    }

}