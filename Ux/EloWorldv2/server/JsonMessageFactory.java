import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import Enum.*;

public class JsonMessageFactory {

    private static JsonMessageFactory instance = null;
    private JsonMessageFactory() {}

    public static JsonMessageFactory getInstance() {
        if (instance == null) {
            instance = new JsonMessageFactory();
        }
        return instance;
    }

    public JSONObject encodeAuthMessage(String username, String password, AuthActions authAction) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH.serialized());
        messageJson.put(MessageStrings.ACTION, authAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.PASSWORD, password);
        return messageJson;
    }

    public JSONObject authServerAnswer(String username, AuthActions serverResponse) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH.serialized());
        //messageJson.put(MessageStrings.ACTION, clientAuthAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.SERVER_RESPONSE, serverResponse.serialized());
        return messageJson;
    }

    public JSONObject onLoginSetupMessage(String username, String memberSince, List<String> friends, UserRoles role, int elo, int refereeScore, String publicKey, String privateKey, JSONObject leaderboard) throws JSONException {
        JSONObject messageJson = new JSONObject();
        JSONArray friendsArray = new JSONArray();
        for (String friend: friends) {
            JSONObject friendObject = new JSONObject();
            friendObject.put(MessageStrings.FRIEND, friend);
            friendsArray.put(friendObject);
        }
        messageJson.put(MessageStrings.DOMAIN, Domain.RESOURCE.serialized());
        messageJson.put(MessageStrings.ACTION, MessageStrings.RESOURCE_SETUP);
        messageJson.put(MessageStrings.MEMBER_SINCE, memberSince);
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.FRIEND, friendsArray);
        messageJson.put(MessageStrings.LEADERBOARD, leaderboard);
        messageJson.put(MessageStrings.ROLE, role.serialized());
        messageJson.put(MessageStrings.ELO, elo);
        messageJson.put(MessageStrings.REFEREE_SCORE, refereeScore);
        messageJson.put(MessageStrings.PUBLIC_KEY, publicKey);
        messageJson.put(MessageStrings.PRIVATE_KEY, privateKey);
        return messageJson;
    }

    public JSONObject friendMessage(String sender, String receiver, FriendReqActions friendAction) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.FRIEND.serialized());
        messageJson.put(MessageStrings.ACTION, friendAction.serialized());
        messageJson.put(MessageStrings.SENDER, sender);
        messageJson.put(MessageStrings.RECEIVER, receiver);
        return messageJson;
    }


    public JSONObject encodeEntryMessage(String winner, String loser) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.CHECK_ENTRY.serialized());
        messageJson.put(MessageStrings.ACTION, MessageStrings.SEND_ENTRY);
        messageJson.put(JsonStrings.WINNER, winner);
        messageJson.put(JsonStrings.LOSER, loser);
        return messageJson;
    }

    public JSONObject entryServerResponse(String winner, String winnerKey, String loser, String loserKey) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.CHECK_ENTRY.serialized());
        messageJson.put(MessageStrings.ACTION, MessageStrings.SEND_ENTRY);
        messageJson.put(JsonStrings.WINNER, winner);
        messageJson.put(JsonStrings.LOSER, loser);
        messageJson.put(JsonStrings.WINNER_KEY, winnerKey);
        messageJson.put(JsonStrings.LOSER_KEY, loserKey);
        return messageJson;
    }

    public JSONObject serverFetchBlockchainRequest() throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.FETCH.serialized());
        return messageJson;
    }

    public JSONObject sendBlockAdded() throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.BLOCK.serialized());
        return messageJson;
    }

    public JSONObject sendBlockchainScoreToServer(int blockchainScore, JSONObject blockchain) throws JSONException {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.BLOCKCHAIN.serialized());
        messageJson.put(MessageStrings.BLOCKCHAIN_SCORE, blockchainScore);
        messageJson.put(MessageStrings.BLOCKCHAIN_OBJECT, blockchain);
        return messageJson;
    }

    public JSONObject sendLeaderBoardToServer(JSONObject leaderBoard) throws Exception {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.LEADERBOARD.serialized());
        messageJson.put(MessageStrings.LEADERBOARD, leaderBoard);
        return messageJson;
    }

}
