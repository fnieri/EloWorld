/**
 * Blockchain Class tasked to parse blocks from db/json and query data from it and add blocks to the blockchain
 * Author : Merian Emile
 * Date 19/02/2023
 */

import Exceptions.UserNotInEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;


public class BlockChain {
    public Block lastBlock;
    public int blockCount;

    /**
     * Constructor used when a Referee class is created
     * @param lastBlockData Path to HEAD.json file needed to direct the blockchain to its last created block
     */
    public BlockChain(String lastBlockData) {
        try {
            JSONObject jsonData = new JSONObject(new String(Files.readAllBytes(Paths.get(lastBlockData))));
            String lastBlockPath = Util.PATH_TO_BLOCKCHAIN_FOLDER + jsonData.getString(JsonStrings.LAST_BLOCK) + ".json";
            blockCount = jsonData.getInt(JsonStrings.BLOCK_NO);
            this.lastBlock = new Block(new String(Files.readAllBytes(Paths.get(lastBlockPath))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            currBlock = Util.convertJsonFileToBlock(currBlock.getPreviousBlockHash());
            playerELO = currBlock.getELO(userPublicKey);
            if (playerELO.found()) {return playerELO.ELO();}
        }
        return Util.BASE_ELO;
    }

    public Hashtable<String, Integer> getLeaderboard() throws UserNotInEntry {
        Block currBlock = lastBlock;
        Hashtable<String, Integer> leaderboard = new Hashtable<>();
        while (!Objects.equals(currBlock.getPreviousBlockHash(), currBlock.getBlockHash())) {
            for (BlockEntry entry: currBlock.getEntries()) {
                String[] players = {entry.player1PublicKey(), entry.player2PublicKey()};
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

    public void addBlock(List<BlockEntry> entries) {
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
        String filename = id + ".json";
        try (FileWriter file = new FileWriter(filename)) {
            file.write(futureBlock.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

