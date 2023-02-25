import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    final static int BASE_ELO = 1000;
    final static String BLOCK_CHAIN_FOLDER = "test";
    final static String PATH_TO_BLOCKCHAIN_FOLDER = System.getProperty("user.dir") + File.separator + BLOCK_CHAIN_FOLDER + File.separator;

    public static Block convertJsonFileToBlock(String filename) {
        String path = PATH_TO_BLOCKCHAIN_FOLDER + filename + ".json";
        try {
            return new Block(new String(Files.readAllBytes(Paths.get(path))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
