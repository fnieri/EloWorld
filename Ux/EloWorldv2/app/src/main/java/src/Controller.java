package src;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import src.Enum.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Controller {
    private Model model;
    private Client client;
    JsonMessageFactory jsonFactory = JsonMessageFactory.getInstance();
    public Controller(Model model, Client client) {
        this.model = model;
        this.client = client;
    }
    public Model getModel() {return model;}

    public void parseRequest(String request) throws Exception {
        JSONObject jsonReq;
        try {
            jsonReq = new JSONObject(request);
        }
        catch (JSONException err) {
            throw new IllegalArgumentException("Wrong request format");
        }

        System.out.println(request);
        String domain = jsonReq.getString(MessageStrings.DOMAIN);
        if (Objects.equals(domain, Domain.AUTH.serialized())) {processAuth(jsonReq);}
        else if (Objects.equals(domain, Domain.RESOURCE.serialized())) {processSetUp(jsonReq);}
        else if (Objects.equals(domain, Domain.FETCH.serialized())) {processServerBlockchainScoreRequest();}
        else if (Objects.equals(domain, Domain.FRIEND.serialized())) {processFriend(jsonReq);}
        else if (Objects.equals(domain, Domain.BLOCKCHAIN.serialized())) {processBlock(jsonReq);}
        else if (Objects.equals(domain, Domain.CHECK_ENTRY.serialized())) {createEntry(jsonReq);}
        else if (Objects.equals(domain, Domain.LEADERBOARD.serialized())) {parseLeaderboard(jsonReq);};
    }

    public void processAuth(JSONObject jsonReq) throws JSONException {

        String serverResponse = jsonReq.getString(MessageStrings.SERVER_RESPONSE);
        if (Objects.equals(serverResponse.toLowerCase(Locale.ROOT), AuthActions.CLIENT_OK_AUTH.serialized())) {
            String username = jsonReq.getString(MessageStrings.USERNAME);
            model.logsIn(username);
        }
    }

    public void processFriend(JSONObject jsonReq) throws JSONException {
        String action = jsonReq.getString(MessageStrings.ACTION);
        String friendToAddRemove = jsonReq.getString(MessageStrings.RECEIVER);
        if (Objects.equals(action, FriendReqActions.FOLLOW_FRIEND.serialized())) {
            model.addFriend(friendToAddRemove);
        }
        else if (Objects.equals(action, FriendReqActions.REMOVE_FRIEND.serialized())) {
            model.removeFriend(friendToAddRemove);
        }
    }

    public void processSetUp(JSONObject jsonReq) throws JSONException, FileNotFoundException {
        String username = jsonReq.getString(MessageStrings.USERNAME);
        JSONArray friends = jsonReq.getJSONArray(MessageStrings.FRIEND);
        List<String> friendsList = new ArrayList<>();

        for (int i = 0; i < friends.length(); i++) {
            String friend = friends.getJSONObject(i).getString(MessageStrings.FRIEND);
            friendsList.add(friend);
        }

        parseLeaderboard(jsonReq.getJSONObject(MessageStrings.LEADERBOARD));

        String memberSince = jsonReq.getString(MessageStrings.MEMBER_SINCE);
        int elo = jsonReq.getInt(MessageStrings.ELO);
        int refereeScore = jsonReq.getInt(MessageStrings.REFEREE_SCORE);
        String serializedRole = jsonReq.getString(MessageStrings.ROLE);
        String publicKey = jsonReq.getString(MessageStrings.PUBLIC_KEY);
        String privateKey = jsonReq.getString(MessageStrings.PRIVATE_KEY);

        UserRoles role = null;
        if (Objects.equals(serializedRole, UserRoles.USER.serialized())) { role = UserRoles.USER;}
        else if (Objects.equals(serializedRole, UserRoles.REFEREE.serialized())) { role = UserRoles.REFEREE;}
        else if (Objects.equals(serializedRole, UserRoles.SUPER_USER.serialized())) { role = UserRoles.SUPER_USER;}

        model.setUp(memberSince, friendsList, role, elo, refereeScore, publicKey, privateKey);
    }

    public void processServerBlockchainScoreRequest() throws JSONException {
        if (model.getRole() == UserRoles.REFEREE) {
            Referee ref = model.getReferee();
            int blockchainScore = ref.getRefereeScore();
            JSONObject blockchain = ref.getBlockchain();

            JSONObject message = jsonFactory.sendBlockchainScoreToServer(blockchainScore, blockchain);
            client.sendMessage(message);
        }
    }

    public void parseLeaderboard(JSONObject jsonReq) throws JSONException {
        try {
            jsonReq = jsonReq.getJSONObject(MessageStrings.LEADERBOARD);
        }
        catch (JSONException e) {}
        List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard = new ArrayList<>();
        List<Map.Entry<String, Integer>> players = new ArrayList<>();
        for (Iterator<String> it = jsonReq.keys(); it.hasNext(); ) {
            String key = it.next();
            players.add(Map.entry(key, jsonReq.getInt(key)));
            if (key.equals(model.getUsername())) {
                model.setELO(jsonReq.getInt(key));
            }
        }
        players.sort(Map.Entry.comparingByValue());
        Collections.reverse(players);
        int position = 1; //First player
        for (Map.Entry<String, Integer> playerEntry: players) {
            leaderboard.add(Map.entry(position, playerEntry));
            position++;
        }
        Collections.reverse(leaderboard);
        model.setLeaderboard(leaderboard);
    }


    public void createEntry(JSONObject jsonReq) throws JSONException, FileNotFoundException {
        String winner = jsonReq.getString(JsonStrings.WINNER);
        String winnerKey = jsonReq.getString(JsonStrings.WINNER_KEY);

        String loser = jsonReq.getString(JsonStrings.LOSER);
        String loserKey = jsonReq.getString(JsonStrings.LOSER_KEY);

        model.createEntry(winner, winnerKey, loser, loserKey);
    }

    public void processBlock(JSONObject jsonReq) throws Exception {
        if (model.getRole() == UserRoles.REFEREE) {
            Referee referee = model.getReferee();
            referee.setBlockchain(jsonReq);
            referee.addBlock();
            client.sendMessage(JsonMessageFactory.getInstance().sendLeaderBoardToServer(referee.getLeaderboard()));
        }
    }
}
