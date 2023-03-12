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
import java.util.Objects;
import Enum.*;

// ClientHandler class
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    ArrayList<ClientHandler> allClients;
    int entryCounter;
    PrintWriter out = null;
    BufferedReader in = null;
    JsonMessageFactory jsonFactory = JsonMessageFactory.getInstance();
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
        catch (IOException e) {
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
        if (Objects.equals(domain, Domain.AUTH.serialized())) {loginHandler(jsonMessage);}
        else if (Objects.equals(domain, Domain.FRIEND.serialized())) {friendsHandler(jsonMessage);}
        else if (Objects.equals(domain, Domain.ENTRY.serialized())) {entryHandler(jsonMessage);}
        else if (Objects.equals(domain, Domain.BLOCKCHAIN.serialized())) {
            //TODO comportement du serveur face a l'arrivée d'une blockchain
            System.out.println("Blockchain");} //on stock une blockchain
    }

    public void sendMessage(JSONObject message) {
        String stringMessage = message.toString();
        out.println(stringMessage);
    }

    public void loginHandler(JSONObject jsonMessage) {
        String action = getActionFromMessage(jsonMessage);
        String username = getUsernameFromMessage(jsonMessage);
        String password = jsonMessage.getString(MessageStrings.PASSWORD);
        boolean authOk = false;
        try {
            if (Objects.equals(action, AuthActions.LOGIN.serialized())) {
                Driver.connection();

                //Driver.auth(username, password);
                authOk = true;
            }
            if (Objects.equals(action, AuthActions.REGISTER.serialized())) {
                //driver
                if (!Driver.nameExists(username)) {
                    Driver.addUser(username, password);
                }
            }
        }
        catch (SQLException sqlException) {
            System.out.println("connexion problem");
        }

        AuthActions authResponse = authOk ? AuthActions.CLIENT_OK_AUTH : AuthActions.CLIENT_BAD_AUTH;
        JSONObject answer = jsonFactory.authServerAnswer(username, authResponse);
        sendMessage(answer);
    }

    public void friendsHandler(JSONObject jsonMessage) {
        String action = getActionFromMessage(jsonMessage);
        String sender = jsonMessage.getString(MessageStrings.SENDER);
        if (Objects.equals(action, FriendReqActions.FOLLOW_FRIEND.serialized())) {
            //TODO Driver add friend
        }
        else if (Objects.equals(action, FriendReqActions.REMOVE_FRIEND.serialized())) {
            //TODO Driver remove friend
        }
    }

    public void entryHandler(JSONObject jsonMessage) {
        entryCounter += 1;
        if ((entryCounter % allClients.size()) == 0){
            sendMessageToAllUsers(JsonMessageFactory.askBlockChain());
        }
    }

    public String getActionFromMessage(JSONObject jsonMessage) {
        return jsonMessage.getString(MessageStrings.ACTION);
    }

    public String getUsernameFromMessage(JSONObject jsonMessage) {
        return jsonMessage.getString(MessageStrings.USERNAME);
    }

    public void sendMessageToAllUsers(JSONObject jsonObject){
        for(ClientHandler cH: allClients) {
            cH.sendMessage(jsonObject);
        }
    }
}