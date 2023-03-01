/**
 * Blockchain Class tasked to parse blocks from db/json and query data from it and add blocks to the blockchain
 * Author : Merian Emile
 * Date 19/02/2023
 */

import Exceptions.UserNotInEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class BlockChain {
    public Block lastBlock;
    public int blockCount;

    /**
     * Constructor used when a Referee class is created
     * @param lastBlockData Path to HEAD.json file needed to direct the blockchain to its last created block
     */
    public BlockChain(String lastBlockData) {
        JSONObject jsonData = Util.convertJsonFileToJSONObject(lastBlockData);
        this.lastBlock = Util.convertJsonFileToBlock(jsonData.getString(JsonStrings.LAST_BLOCK));
        blockCount = jsonData.getInt(JsonStrings.BLOCK_NO);
    }

    public int getScore() {
        Block currBlock = lastBlock;
        int numBlock = 0;
        int numEntries = 0;
        // Data Count
        boolean endNotReached = true;
        while (endNotReached) {
            if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                endNotReached = false;
            }

            numBlock += 1;
            numEntries += currBlock.getScore();

            currBlock = Util.convertJsonFileToBlock(currBlock.getPreviousBlockHash());
        }
        return numEntries/numBlock;
    }

    public int getELO(String userPublicKey) {
        Block currBlock = lastBlock;
        PlayerSearch playerELO;
        boolean endNotReached = true;
        while (endNotReached) {
            if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                endNotReached = false;
            }

            playerELO = currBlock.getELO(userPublicKey);
            if (playerELO.found()) {return playerELO.ELO();}

            currBlock = Util.convertJsonFileToBlock(currBlock.getPreviousBlockHash());
        }
        return Util.BASE_ELO;
    }

    public JSONObject getLeaderboard() throws UserNotInEntry {
        Block currBlock = lastBlock;
        JSONObject leaderboard = new JSONObject();

        boolean endNotReached = true;
        while (endNotReached) {
            if (Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
                endNotReached = false;
            }

            for (BlockEntry entry: currBlock.getEntries()) {
                String[] players = {entry.player1PublicKey(), entry.player2PublicKey()};
                for (String player: players) {
                    if (!leaderboard.has(player)) {
                        leaderboard.put(player, entry.getPlayerELO(player));
                    }
                }
            }

            currBlock = Util.convertJsonFileToBlock(currBlock.getPreviousBlockHash());

        }
        return leaderboard;
    }

    public void addBlock(ArrayList<BlockEntry> entries) {
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
        String filename = Util.PATH_TO_BLOCKCHAIN_FOLDER + id + ".json";
        try (FileWriter file = new FileWriter(filename)) {
            file.write(futureBlock.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update with new information
        lastBlock = Util.convertJsonFileToBlock(id);
        blockCount = block_no;


        JSONObject updateHead = new JSONObject();
        updateHead.put(JsonStrings.LAST_BLOCK, lastBlock.getBlockHash());
        updateHead.put(JsonStrings.BLOCK_NO, blockCount);
        String headFile = Util.PATH_TO_BLOCKCHAIN_FOLDER + "HEAD.json";
        try (FileWriter file = new FileWriter(headFile)) {
            file.write(updateHead.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

