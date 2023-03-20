package src;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import src.SQLDriver;
import java.util.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import src.Enum.*;

// ClientHandler class
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    ArrayList<ClientHandler> allClients;

    ArrayList<JSONObject> receivedBlockChains;
    JSONObject leaderBoard;
    PrintWriter out = null;
    BufferedReader in = null;
    JsonMessageFactory jsonFactory = JsonMessageFactory.getInstance();
    //static SQLDriver SQLDriver = new SQLDriver();

    // Constructor
    public ClientHandler(Socket socket, ArrayList<ClientHandler> connectedClients, ArrayList<JSONObject> receivedBlockChains, JSONObject leaderBoard) {
        this.clientSocket = socket;
        this.allClients = connectedClients;
        this.receivedBlockChains = receivedBlockChains;
        this.leaderBoard = leaderBoard;
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
        else if (Objects.equals(domain, Domain.BLOCK.serialized())) {
            blockHandler();}
        else if (Objects.equals(domain, Domain.BLOCKCHAIN.serialized())) {
            receivedBlockChains.add(jsonMessage);}
        else if (Objects.equals(domain, Domain.LEADERBOARD.serialized())) {
            sendMessageToAllUsers(jsonMessage);}
        else if (Objects.equals(domain, Domain.CHECK_ENTRY.serialized())) {checkEntryValidity(jsonMessage);}
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
        SQLDriver.connection();
        if (Objects.equals(action, AuthActions.LOGIN.serialized())) {
            authOk = SQLDriver.auth(username, password);

        }
        if (Objects.equals(action, AuthActions.REGISTER.serialized())) {
            //SQLDriver
            if (!SQLDriver.nameExists(username)) {
                // https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal
                Date date = new Date();
                int year = 0, month = 0, day = 0;
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                year  = localDate.getYear();
                month = localDate.getMonthValue();
                day = localDate.getDayOfMonth();

                String memberSinceDate = day + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                SQLDriver.addUser(username, password, memberSinceDate);
                authOk = true;
            }
        }


        AuthActions authResponse = authOk ? AuthActions.CLIENT_OK_AUTH : AuthActions.CLIENT_BAD_AUTH;
        JSONObject answer = jsonFactory.authServerAnswer(username, authResponse);
        sendMessage(answer);

        if (authOk) {
            SQLDriver.connection();
            String memberDate = SQLDriver.getMemberDate(username);

            List<String> friends = SQLDriver.getFriendsList(username);

            String role = SQLDriver.getRole(username);
            UserRoles playerRole;
            if (Objects.equals(role, "user")) playerRole = UserRoles.USER;
            else playerRole = UserRoles.REFEREE;

            int ELO = 1500;
            int refereeScore = 0;

            String publicKey = SQLDriver.getModulus(username);
            String privateKey = SQLDriver.getPublicExponent(username);
            JSONObject setUpMessage = jsonFactory.onLoginSetupMessage(username, memberDate, friends, playerRole, ELO, refereeScore, publicKey, privateKey, this.leaderBoard);
            System.out.println(setUpMessage);
            sendMessage(setUpMessage);
        }
        SQLDriver.closeConnection();

    }

    public void checkEntryValidity(JSONObject jsonMessage) throws JSONException, SQLException {
        String winner = jsonMessage.getString(JsonStrings.WINNER);
        String loser = jsonMessage.getString(JsonStrings.LOSER);
        SQLDriver.connection();
        boolean entryOK = SQLDriver.nameExists(winner) && SQLDriver.nameExists(loser);
        SQLDriver.closeConnection();
        if (entryOK) sendMessage(jsonMessage); //Resene message as is to user
    }

    public void friendsHandler(JSONObject jsonMessage) throws JSONException, SQLException {
        String action = getActionFromMessage(jsonMessage);
        String sender = jsonMessage.getString(MessageStrings.SENDER);
        String receiver = jsonMessage.getString(MessageStrings.RECEIVER);
        JSONObject messageFriend = new JSONObject();
        SQLDriver.connection();

        //If SQLDriver exists and friend exists // TODO
        if (Objects.equals(action, FriendReqActions.REMOVE_FRIEND.serialized())) {
            SQLDriver.removeFriend(sender, receiver);
        }
        else if (Objects.equals(action, FriendReqActions.FOLLOW_FRIEND.serialized())) {
            SQLDriver.addFriend(sender, receiver);
        }

        SQLDriver.closeConnection();
        sendMessage(jsonMessage); // resend message as is to client
    }

    public void blockHandler() throws JSONException {
        sendMessageToAllUsers(JsonMessageFactory.getInstance().serverFetchBlockchainRequest());
        Timer t = new Timer();
        getBestBlockchain bestBlockchain = new getBestBlockchain(receivedBlockChains, this);
        t.schedule(bestBlockchain, 10000);
    }

    public String getActionFromMessage(JSONObject jsonMessage) throws JSONException {
        return jsonMessage.getString(MessageStrings.ACTION);
    }

    public String getUsernameFromMessage(JSONObject jsonMessage) throws JSONException {
        return jsonMessage.getString(MessageStrings.USERNAME);
    }

    public void sendMessageToAllUsers(JSONObject jsonObject) {
        for (ClientHandler cH : allClients) {
            cH.sendMessage(jsonObject);
        }
    }

    static class getBestBlockchain extends TimerTask {
        ArrayList<JSONObject> challengers;

        ClientHandler clientHandler;
        int currBestScore = 0;
        JSONObject champion = null;
        public getBestBlockchain(ArrayList<JSONObject> challengerList, ClientHandler clientHandler){
            this.challengers = challengerList;
            this.clientHandler = clientHandler;
        }
        public void tournament() throws JSONException {
            for (JSONObject challenger : challengers) {
                if (challenger.getInt(MessageStrings.BLOCKCHAIN_SCORE) >= currBestScore) {
                    champion = challenger;
                    currBestScore = champion.getInt(MessageStrings.BLOCKCHAIN_SCORE);
                }
            }
        }

        public void run() {
            try {
                tournament();
                clientHandler.sendMessage(JsonMessageFactory.getInstance().
                                sendBlockchainScoreToServer(this.currBestScore, this.champion));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}