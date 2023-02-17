/**
 * Class that parses data from a block to create a list of block entries
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

import java.util.ArrayList;
import java.util.List;

public class EntryParser {

    static public List<BlockEntry> createEntries(String data) {
        BlockEntry block = new  BlockEntry(1, 2, "aa", "bb", 12, "cc", 13, "dd", 14);
        List<BlockEntry> blocks = new ArrayList<>();
        blocks.add(block);
        return blocks;
    }
}
