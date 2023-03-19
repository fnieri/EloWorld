package src;
import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {

    private static Util instance = null;
    private Util() {}

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    int BASE_ELO = 0;
    String SUFFIX = ".json";
    String BLOCKCHAIN_HEAD = "HEAD";
    String FIRST_BLOCK = "GenesisBlock";
    String BLOCK_CHAIN_FOLDER = "test";
    String ENTRIES_FOLDER = "entries";
    @SuppressLint("SdCardPath") //??
    File  PATH_TO_BLOCKCHAIN_FOLDER = new File("/data/user/0/com.example.eloworld/files/");
    String PATH_TO_ENTRIES_FOLDER = ENTRIES_FOLDER + File.separator;

    public void setPathToBlockChain(Context context) {
        PATH_TO_BLOCKCHAIN_FOLDER = context.getFilesDir();
    }

    public String getPathToBlockChain() {
        return PATH_TO_BLOCKCHAIN_FOLDER.toString();
    }

    /**
     * This Utility method is used to read .json files of the blockchain
     * @param filename : name of the .json file in the blockchain folder to read
     * @return Content of the file as a String
     */
    public String convertJsonFileToString(String filename) {

        String path = PATH_TO_BLOCKCHAIN_FOLDER + File.separator + filename;

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
    public  Block convertJsonFileToBlock(String filename) throws JSONException {
        return new Block(convertJsonFileToString(filename));
    }

    /**
     * This Utility method is used to read .json files of the blockchain
     * @param filename : name of the .json file in the blockchain folder to read
     * @return Content of the file as a JSONObject
     */
    public JSONObject convertJsonFileToJSONObject(String filename) throws JSONException {
        return new JSONObject(convertJsonFileToString(filename));
    }

    /**
     * Writes a new file, overwrites any existing file of the same name
     * @param content content of the file
     * @param path path where the file will be written
     */
    public  void writeJSONFile(String content, String path) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(path, false);
        FileWriter fw;
        try  {
            fw = new FileWriter(fos.getFD());
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}