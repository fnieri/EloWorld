package src;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import src.Enum.*;
import src.Enum.AuthActions;
import src.Enum.Domain;
import src.Enum.FriendReqActions;
import src.Enum.UserRoles;

// ClientHandler class
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    ArrayList<ClientHandler> allClients;
    int entryCounter;
    PrintWriter out = null;
    BufferedReader in = null;
    JsonMessageFactory jsonFactory = JsonMessageFactory.getInstance();
    static User user = null;
    //static Driver driver = new Driver();

    // Constructor
    public ClientHandler(Socket socket, ArrayList<ClientHandler> connectedClients, int entryCounter) {
        this.clientSocket = socket;
        this.allClients = connectedClients;
        this.entryCounter = entryCounter;
    }

    public void run() {
        try {

            // get the outputstream of client
            out = new PrintWriter(
                    clientSocket.getOutputStream(), true);

            // get the inputstream of client
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));

            String packet;
            while ((packet = in.readLine()) != null) {
                parsePacket(packet);
            }
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            allClients.remove(this);
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parsePacket(String userMessage) throws JSONException {
        JSONObject jsonMessage;
        try {
            jsonMessage = new JSONObject(userMessage);
        }
        catch (JSONException err) {
            throw new IllegalArgumentException("Wrong request format");
        }
        System.out.println(jsonMessage.toString());
        String domain = jsonMessage.getString(MessageStrings.DOMAIN);
        if (Objects.equals(domain, Domain.AUTH.serialized())) {loginHandler(jsonMessage);}
        else if (Objects.equals(domain, Domain.FRIEND.serialized())) {friendsHandler(jsonMessage);}
    }

    public void sendMessage(JSONObject message) {
        String stringMessage = message.toString();
        out.println(stringMessage);
    }

    public void loginHandler(JSONObject jsonMessage) throws JSONException {
        String action = getActionFromMessage(jsonMessage);
        String username = getUsernameFromMessage(jsonMessage);
        String password = jsonMessage.getString(MessageStrings.PASSWORD);
        boolean authOk = false;
        //  try {
        if (Objects.equals(action, AuthActions.LOGIN.serialized())) {
            Driver.connection();

            //Driver.auth(username, password);
            authOk = true;
        }
        if (Objects.equals(action, AuthActions.REGISTER.serialized())) {
            //driver
            // if (!Driver.nameExists(username)) {
            //   Driver.addUser(username, password);
            authOk = true;
        }
        //  }
        // }
        // catch (SQLException sqlException) {
        //   System.out.println("connexion problem");
        //}

        AuthActions authResponse = authOk ? AuthActions.CLIENT_OK_AUTH : AuthActions.CLIENT_BAD_AUTH;
        JSONObject answer = jsonFactory.authServerAnswer(username, authResponse);
        sendMessage(answer);

        if (authOk) {
            String memberSince = "12/01/2023"; //Driver.getMemberSince(username)
            List<String> friends = Arrays.asList("emile", "theo", "elliot");
            UserRoles role = UserRoles.USER;
            int ELO = 1500;
            int refereeScore = 0;
            String publicKey = "asdisajd";
            String privateKey = "iiufiufd";
            List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard =
                    List.of(Map.entry(1, Map.entry("emile", 1500)),
                            Map.entry(2, Map.entry("fnieri", 1499)),
                            Map.entry(3, Map.entry("theo", 1498)),
                            Map.entry(4, Map.entry("elliot", 1497)));
            JSONObject setUpMessage = jsonFactory.onLoginSetupMessage(username, memberSince, friends, role, ELO, refereeScore, publicKey, privateKey, leaderboard);
            System.out.println(setUpMessage);
            sendMessage(setUpMessage);
        }
    }

    public void friendsHandler(JSONObject jsonMessage) throws JSONException {
        String action = getActionFromMessage(jsonMessage);
        String sender = jsonMessage.getString(MessageStrings.SENDER);
        String receiver = jsonMessage.getString(MessageStrings.RECEIVER);
        JSONObject messageFriend = new JSONObject();
        //If driver exists and friend exists // TODO
        sendMessage(jsonMessage); // resend message as is to client
    }

    public void entryHandler(JSONObject jsonMessage) {
        entryCounter += 1;
        if (entryCounter % allClients.size() == 0) {
            sendMessageToAllUsers(new JSONObject()); //TODO ajouter le fetch des blockchain
        }
    }

    public String getActionFromMessage(JSONObject jsonMessage) throws JSONException {
        return jsonMessage.getString(MessageStrings.ACTION);
    }

    public String getUsernameFromMessage(JSONObject jsonMessage) throws JSONException {
        return jsonMessage.getString(MessageStrings.USERNAME);
    }

    public void sendMessageToAllUsers(JSONObject jsonObject){
        for(ClientHandler cH: allClients) {
            cH.sendMessage(jsonObject);
        }
    }
}