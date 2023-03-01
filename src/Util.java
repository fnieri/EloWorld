import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    final static int BASE_ELO = 0;
    final static String BLOCKCHAIN_HEAD = "HEAD";
    final static String FIRST_BLOCK = "GenesisBlock";
    final static String BLOCK_CHAIN_FOLDER = "blockchain";
    final static String PATH_TO_BLOCKCHAIN_FOLDER = System.getProperty("user.dir") + File.separator + BLOCK_CHAIN_FOLDER + File.separator;

    /**
     * This Utility method is used to read .json files of the blockchain
     * @param filename : name of the .json file in the blockchain folder to read
     * @return Content of the file as a String
     */
    public static String convertJsonFileToString(String filename) {
        String path = PATH_TO_BLOCKCHAIN_FOLDER + filename + ".json";
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This Utility method is used to read .json files of the blockchain
     * @param filename : name of the .json file in the blockchain folder to read
     * @return Content of the file as a Block
     */
    public static Block convertJsonFileToBlock(String filename) {
        return new Block(convertJsonFileToString(filename));
    }
    /**
     * This Utility method is used to read .json files of the blockchain
     * @param filename : name of the .json file in the blockchain folder to read
     * @return Content of the file as a JSONObject
     */
    public static JSONObject convertJsonFileToJSONObject(String filename) {
        return new JSONObject(convertJsonFileToString(filename));
    }



}
