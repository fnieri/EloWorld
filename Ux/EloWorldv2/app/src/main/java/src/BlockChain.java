/**
 * Blockchain Class tasked to parse blocks from db/json and query data from it and add blocks to the blockchain
 * Author : Merian Emile
 * Date 19/02/2023
 */

package src;
import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;


public class BlockChain {
    public Block lastBlock;
    public int blockCount;
    Util util = Util.getInstance();

    /**
     * Constructor used when a Referee class is created
     */
    public BlockChain() throws JSONException {

        JSONObject jsonData = util.convertJsonFileToJSONObject(util.BLOCKCHAIN_HEAD + util.SUFFIX);
        this.lastBlock = util.convertJsonFileToBlock(jsonData.getString(JsonStrings.LAST_BLOCK) + util.SUFFIX);
        blockCount = jsonData.getInt(JsonStrings.BLOCK_NO);
    }

    /**
     * returns the score of the blockchain, calculated as the number of entries. A more complex calculation to ensure
     * blocks are at max capacity is required.
     *
     * @return The score of the blockchain
     */
    public int getScore() throws JSONException {
        Block currBlock = lastBlock;
        int numEntries = 0;
        // Data Count
        boolean endNotReached = true;
        while (endNotReached) {
            if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                endNotReached = false;
            }

            numEntries += currBlock.getScore();
            currBlock = util.convertJsonFileToBlock(currBlock.getPreviousBlockHash() + util.SUFFIX);

        }
        return numEntries;
    }

    public Block getBlock(int index) throws Exception {
        try {
            Block currBlock = lastBlock;
            for (int i = 0; i < index; i++) {
                if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                    throw new IndexOutOfBoundsException("Block index out of blockchain bounds !");
                }
                currBlock = new Block(currBlock.getPreviousBlockHash());
            }
            return currBlock;
        } catch (IndexOutOfBoundsException e) {
           System.out.println(e.getMessage());
           return null;
        }
    }

    /**
     * formats the blockchain into a leaderboard listing all players and their most recent ELO recorded in the blockchain.
     *
     * @return JSONObject format of a leaderboard parsed from the Blockchain
     */
    public JSONObject getLeaderboard() throws Exception {
        JSONObject leaderboard = new JSONObject();
        Stack<String[]> matches = getMatchHistory();

        while (!matches.isEmpty()) {
            String[] players = matches.pop();
            for (String player: players) {
                if (!leaderboard.has(player)) {
                    leaderboard.put(player, util.BASE_ELO);
                }
                ELOCalculator eloCalculator = new ELOCalculator(new double[]{leaderboard.getDouble(players[0]), leaderboard.getDouble(players[1])}, new boolean[]{true, false});
                double[] newElos = eloCalculator.calculateELOs();
                leaderboard.put(players[0], newElos[0]);
                leaderboard.put(players[1], newElos[1]);
            }
        }

        return leaderboard;
    }

    @NonNull
    private Stack<String[]> getMatchHistory() throws JSONException {
        Block currBlock = lastBlock;
        Stack<String[]> matches = new Stack<>();
        boolean endNotReached = true;
        while (endNotReached) {
            if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                endNotReached = false;
            }
            for (BlockEntry entry: currBlock.getEntries()) {
                matches.push(new String[]{entry.getWinnerPublicKey(), entry.getLoserPublicKey()});
            }
            currBlock = util.convertJsonFileToBlock(currBlock.getPreviousBlockHash());
        }
        return matches;
    }

    /**
     * creates a new block from a list of entries and add it to the blockchain
     *
     * @param entries add a block
     */
    public void addBlock(ArrayList<BlockEntry> entries) throws JSONException {
        JSONObject futureBlock = new JSONObject();
        int block_no = blockCount + 1;
        // block id
        String id = "Block" + block_no;
        futureBlock.put(JsonStrings.BLOCK_HASH, id);
        // previous_block id
        futureBlock.put(JsonStrings.PARENT_BLOCK_HASH, lastBlock.getBlockHash());
        // Entries
        JSONArray jsonEntries = new JSONArray();
        for (BlockEntry entry: entries) {
            jsonEntries.put(entry.asJson());
        }
        futureBlock.put(JsonStrings.ENTRIES, jsonEntries);
        // Write new Block as .json file
        String filename = util.getPathToBlockChain() + File.separator + id + util.SUFFIX;
        try (FileWriter file = new FileWriter(filename)) {
            file.write(futureBlock.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Update with new information
        lastBlock = util.convertJsonFileToBlock(id + util.SUFFIX);
        blockCount = block_no;
        JSONObject updateHead = new JSONObject();
        updateHead.put(JsonStrings.LAST_BLOCK, lastBlock.getBlockHash());
        updateHead.put(JsonStrings.BLOCK_NO, blockCount);
        String headFile = util.getPathToBlockChain() + util.BLOCKCHAIN_HEAD + util.SUFFIX;
        try (FileWriter file = new FileWriter(headFile)) {
            file.write(updateHead.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return blockCount;
    }

}

