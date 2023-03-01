import org.json.JSONArray;
import org.json.JSONObject;
import Enum.*;

import java.util.List;

public class JsonMessageFactory {

    private static JsonMessageFactory instance = null;
    private JsonMessageFactory() {}

    public JsonMessageFactory getInstance() {
        if (instance == null) {
            instance = new JsonMessageFactory();
        }
        return instance;
    }

    public JSONObject encodeAuthMessage(String username, String password, AuthActions authAction, UserRoles role) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH.serialized());
        messageJson.put(MessageStrings.ACTION, authAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.PASSWORD, password);
        return messageJson;
    }

    public JSONObject authServerAnswer(String username, AuthActions clientAuthAction, AuthActions serverResponse) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH.serialized());
        messageJson.put(MessageStrings.ACTION, clientAuthAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.SERVER_RESPONSE, serverResponse);
        return messageJson;
    }

    public JSONObject onLoginSetupMessage(String username, String memberSince, List<String> friends, UserRoles role, int elo, int refereeScore) {
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
        messageJson.put(MessageStrings.FRIEND, friends);
        messageJson.put(MessageStrings.ROLE, role.serialized());
        messageJson.put(MessageStrings.ELO, elo);
        messageJson.put(MessageStrings.REFEREE_SCORE, refereeScore);
        return messageJson;
    }

    public JSONObject followFriendMessage(String sender, String receiver, FriendReqActions friendAction) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.FRIEND.serialized());
        messageJson.put(MessageStrings.ACTION, friendAction.serialized());
        messageJson.put(MessageStrings.SENDER, sender);
        messageJson.put(MessageStrings.RECEIVER, receiver);
        return messageJson;
    }
    public JSONObject encodeEntryMessage(String refereeKey, int refereeScore, String player1Key, int player1ELO, String player2Key, int player2ELO) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.ENTRY.serialized());
        messageJson.put(MessageStrings.ACTION, MessageStrings.SEND_ENTRY);
        messageJson.put(JsonStrings.REFEREE_KEY, refereeKey);
        messageJson.put(JsonStrings.REFEREE_SCORE, refereeScore);
        messageJson.put(JsonStrings.PLAYER_1_KEY, player1Key);
        messageJson.put(JsonStrings.PLAYER_1_ELO, player1ELO);
        messageJson.put(JsonStrings.PLAYER_2_KEY, player2Key);
        messageJson.put(JsonStrings.PLAYER_2_ELO, player2ELO);
        return messageJson;
    }


}
