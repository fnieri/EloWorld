/**
 * Class representing a block in the blockchain
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import java.util.List;
import java.util.Comparator;

class Block {
    String blockHash;
    String previousBlockHash;
    String data;
    List<BlockEntry> entries;

    public Block(String blockHash, String previousBlockHash, String data) {
        this.blockHash = blockHash;
        this.previousBlockHash = previousBlockHash;
        this.data = data;
        this.entries = getEntriesFromData();
    }

    /**
     * Calculate the score of a block
     * The score of a block is defined as the sum on all entries of the referee scores
     * @return score: The score of the block
     */
    public int calculateScore() {
        int score = 0;
        for (BlockEntry entry : entries) {
            score += entry.getEntryScore();
        }
        return score;
    }

    public int getScore() {
        return calculateScore();
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    /**
     * Sort block entries by their id in the list of entries
     */
    private void sortEntriesByID() {
        entries.sort(Comparator.comparing(BlockEntry::getEntryID));
    }

    /**
     * Sort block entries by their timestamp
     */
    private void sortEntriesByTimestamp() {
        entries.sort(Comparator.comparing(BlockEntry::getTimestamp));
    }

    /**
     * Return a list of blockEntries from block data
     */
    private List<BlockEntry> getEntriesFromData() {
        return EntryParser.createEntries(data);
    }
}