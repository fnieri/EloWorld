import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;
import Enum.*;

// Client class
class Client {

    BufferedReader in;
    PrintWriter out;
    static User user = null;

    // driver code
    public void main(String[] args) {

        // establish a connection by providing host and port 8080
        try (Socket socket = new Socket("localhost", 8080)) {

            // reading from server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            String line;

            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '/') {
                    if (user instanceof Referee) {
                        System.out.println("Referee");
                        //out.println(user.getLeaderboard);
                    }
                } else {
                    System.out.print(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        JSONObject jsonObject = new JSONObject(message);
        out.println(jsonObject);
    }

    public void parsePacket(String userMessage) {
        System.out.println("entered server");
        JSONObject jsonMessage;
        try {
            jsonMessage = new JSONObject(userMessage);
        }
        catch (JSONException err) {
            throw new IllegalArgumentException("Wrong request format");
        }
        System.out.println(jsonMessage);
        String domain = jsonMessage.getString(MessageStrings.DOMAIN);
        if (Objects.equals(domain, Domain.AUTH.serialized())) {System.out.println("connexion établie");}
        else if (Objects.equals(domain, Domain.FRIEND.serialized())) {System.out.println("Ami ajouté");}
    }

}