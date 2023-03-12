import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

import Enum.AuthActions;
import Enum.Domain;

// Client class
public class Client {

    PrintWriter out;
    BufferedReader in;
    static User user = null;
    Socket socket;
    Model model = new Model();
    //Controller controller = new Controller(model);

    public Client() throws IOException {
        model.setUsername("fnieri");
    }

    // driver code
    public void main() {

        // establish a connection by providing host and port 8080
        try (Socket socket = new Socket("localhost", 8080)) {

            // reading from server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            String line;

            while ((line = in.readLine()) != null) {
                parsePacket(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        JSONObject jsonObject = new JSONObject(message);
        out.println(jsonObject);

    }

    public void parsePacket(String message) {

    }

}