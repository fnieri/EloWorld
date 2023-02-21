import org.json.JSONObject;
import Enum.*;

public class JsonMessageFactory {

    private static JsonMessageFactory instance = null;
    private JsonMessageFactory() {}

    public JsonMessageFactory getInstance() {
        if (instance == null) {
            instance = new JsonMessageFactory();
        }
        return instance;
    }

    public JSONObject encodeAuthMessage(String username, String password, AuthActions authAction) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH);
        messageJson.put(MessageStrings.ACTION, authAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.PASSWORD, password);
        return messageJson;
    }

    public JSONObject authServerAnswer(String username, AuthActions clientAuthAction, AuthActions serverResponse) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.AUTH);
        messageJson.put(MessageStrings.ACTION, clientAuthAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.SERVER_RESPONSE, serverResponse);
        return messageJson;
    }

    public JSONObject friendRequestMessage(String sender, String receiver, FriendReqActions friendAction) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.DOMAIN, Domain.FRIEND);
        messageJson.put(MessageStrings.ACTION, MessageStrings.FRIEND_REQUEST);
        messageJson.put(MessageStrings.FRIEND_ACTION, friendAction.serialized());
        messageJson.put(MessageStrings.SENDER, sender);
        messageJson.put(MessageStrings.RECEIVER, receiver);
        return messageJson;
    }
    public JSONObject encodeEntryMessage(String refereeKey, String player1Key, String player1ELO, String player2Key, String player2ELO) {
        JSONObject messageJson = new JSONObject();

        messageJson.put(MessageStrings.DOMAIN, Domain.ENTRY);
        messageJson.put(MessageStrings.ACTION, MessageStrings.SEND_ENTRY);
        messageJson.put(JsonStrings.REFEREE_KEY, refereeKey);
        messageJson.put(JsonStrings.PLAYER_1_KEY, player1Key);
        messageJson.put(JsonStrings.PLAYER_1_ELO, player1ELO);
        messageJson.put(JsonStrings.PLAYER_2_KEY, player2Key);
        messageJson.put(JsonStrings.PLAYER_2_ELO, player2Key);
        return messageJson;
    }


}
