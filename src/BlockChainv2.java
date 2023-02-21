import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockChainv2 {
    private List<Block> blocks;

    private Block lastBlock;
    private String lastBlockData;



    public void Blockchain(String lastBlockData) {
        this.blocks = new ArrayList<>();
        this.lastBlockData = lastBlockData;
        lastBlock = new Block(getLastBlockID());

    }

    public void loadBlockchain(String lastBlockData) throws IOException {
        Block block = new Block(lastBlockData);
        blocks.add(block);
        while (!Objects.equals(block.getBlockHash(), block.getPreviousBlockHash())) {
            block = new Block(getBlockFile(block.getPreviousBlockHash()));
            blocks.add(block);
        }
    }

    public String getBlockFile(String blockHash) throws IOException {
        //May need to rewrite this
        String path = System.getProperty("user.dir") + "\\" + blockHash + ".json";
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public int getScore() {
       int score = 0;
        for (Block block: blocks) {
            score += block.getScore();
        }
        return score/blocks.size();

    }

    public int getELO(String userPublicKey) {
        PlayerSearch playerELO;
        for (Block block: blocks) {
            playerELO = block.getELO(userPublicKey);
            if (playerELO.found()) {return playerELO.ELO();}
        }
        return Util.BASE_ELO;
    }

    public void addBlock(Block block) {
        blocks.add(0, block);
    }

    public String getLastBlockID() {return new JSONObject(lastBlockData).getString(JsonStrings.LAST_BLOCK);}

}
