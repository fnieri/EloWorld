package src;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

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
        catch (IOException | JSONException | SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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

    public void parsePacket(String userMessage) throws JSONException, SQLException, NoSuchAlgorithmException {
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

    public void loginHandler(JSONObject jsonMessage) throws JSONException, SQLException, NoSuchAlgorithmException {
        String action = getActionFromMessage(jsonMessage);
        String username = getUsernameFromMessage(jsonMessage);
        String password = jsonMessage.getString(MessageStrings.PASSWORD);
        boolean authOk = false;
        Driver.connection();
        if (Objects.equals(action, AuthActions.LOGIN.serialized())) {
            authOk = Driver.auth(username, password);
            
        }
        if (Objects.equals(action, AuthActions.REGISTER.serialized())) {
            //driver
            if (!Driver.nameExists(username)) {
                // https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal
                Date date = new Date();
                int year = 0, month = 0, day = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    year  = localDate.getYear();
                    month = localDate.getMonthValue();
                    day = localDate.getDayOfMonth();
                }
                String memberSinceDate = day + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                Driver.addUser(username, password, memberSinceDate);
                authOk = true;
            }
        }


        AuthActions authResponse = authOk ? AuthActions.CLIENT_OK_AUTH : AuthActions.CLIENT_BAD_AUTH;
        JSONObject answer = jsonFactory.authServerAnswer(username, authResponse);
        sendMessage(answer);

        if (authOk) {
            Driver.connection();
            String memberDate = Driver.getMemberDate(username);

            List<String> friends = Driver.getFriendsList(username);

            String role = Driver.getRole(username);
            UserRoles playerRole;
            if (Objects.equals(role, "user")) playerRole = UserRoles.USER;
            else playerRole = UserRoles.REFEREE;

            int ELO = 1500;
            int refereeScore = 0;

            String publicKey = Driver.getPublicKey(username);
            String privateKey = Driver.getPrivateKey(username);
            List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard =
                    List.of(Map.entry(1, Map.entry("emile", 1500)),
                            Map.entry(2, Map.entry("fnieri", 1499)),
                            Map.entry(3, Map.entry("theo", 1498)),
                            Map.entry(4, Map.entry("elliot", 1497)));
            JSONObject setUpMessage = jsonFactory.onLoginSetupMessage(username, memberDate, friends, playerRole, ELO, refereeScore, publicKey, privateKey, leaderboard);
            System.out.println(setUpMessage);
            sendMessage(setUpMessage);
        }
        Driver.closeConnection();

    }

    public void friendsHandler(JSONObject jsonMessage) throws JSONException, SQLException {
        String action = getActionFromMessage(jsonMessage);
        String sender = jsonMessage.getString(MessageStrings.SENDER);
        String receiver = jsonMessage.getString(MessageStrings.RECEIVER);
        JSONObject messageFriend = new JSONObject();
        Driver.connection();

        //If driver exists and friend exists // TODO
        if (Objects.equals(action, FriendReqActions.REMOVE_FRIEND.serialized())) {
            //TODO(elliot)
        }
        else if (Objects.equals(action, FriendReqActions.FOLLOW_FRIEND.serialized())) {
            Driver.addFriend(sender, receiver);
        }



        Driver.closeConnection();
        sendMessage(jsonMessage); // resend message as is to client
    }

    public void entryHandler(JSONObject jsonMessage) throws JSONException {
        entryCounter += 1;
        if (entryCounter >= allClients.size()) {
            sendMessageToAllUsers(JsonMessageFactory.getInstance().serverFetchBlockchainRequest()); //TODO ajouter le fetch des blockchain

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