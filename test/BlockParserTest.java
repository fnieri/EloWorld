import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlockParserTest {

    String file = getBlock1();
    BlockParser parser = new BlockParser(file);

    public BlockParserTest() throws Exception {
    }


    public static String getBlock1() throws Exception {
        String block1Path = System.getProperty("user.dir") + "\\test\\Block1.json";
        return fileSetUp(block1Path);
    }

    public static String fileSetUp(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)));
       // System.out.println(json);
        return json;
    }

    @Test
    public void testWrongFile() throws Exception {
        String badFile = System.getProperty("user.dir") + "\\test\\MisformatedBlock.json";
        String file = fileSetUp(badFile);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new BlockParser(file));
        assertEquals("Wrong json file format", exception.getMessage());

    }



    @Test
    public void testBlockHash() throws Exception {

        String blockHash = parser.getBlockHash();
        assertEquals("Block1", blockHash);

    }

    @Test
    public void testPreviousBlockHash() throws Exception {
        String blockHash = parser.getPreviousBlockHash();
        assertEquals("Block1", blockHash);

    }

    @Test
    public void testEntriesSize() throws Exception {
        JSONArray entries =  parser.getJsonEntries();
        assertEquals(entries.length(), 2);
    }

    @Test
    public void testFirstEntry() throws Exception {
        JSONArray entries = parser.getJsonEntries();
        JSONObject firstEntry = entries.getJSONObject(0);
        BlockEntry entryAsBlock = parser.getEntryFromJsonObject(firstEntry);
        assertEquals(entryAsBlock.getTimestamp(),1);
        assertEquals(entryAsBlock.getRefereeKey(), "fran11");
        assertEquals(entryAsBlock.getRefereeScore(), 11);
        assertEquals(entryAsBlock.getPlayer1PublicKey(), "emile11");
        assertEquals(entryAsBlock.getPlayer1ELO(), 140);
        assertEquals(entryAsBlock.getPlayer2PublicKey(), "theo11");
        assertEquals(entryAsBlock.getPlayer2ELO(), 150);
    }

}
