import org.json.JSONObject;
import Enum.*;

public class JsonMessageFactory {

    public static JSONObject encodeAuthMessage(String username, String password, AuthActions authAction) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.ACTION, authAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.PASSWORD, password);
        return messageJson;
    }

    public static JSONObject authServerAnswer(String username, AuthActions clientAuthAction, AuthActions serverResponse) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.ACTION, clientAuthAction.serialized());
        messageJson.put(MessageStrings.USERNAME, username);
        messageJson.put(MessageStrings.SERVER_RESPONSE, serverResponse);
        return messageJson;
    }

    public static JSONObject friendRequestMessage(String sender, String receiver, FriendReqActions friendAction) {
        JSONObject messageJson = new JSONObject();
        messageJson.put(MessageStrings.ACTION, MessageStrings.FRIEND_REQUEST);
        messageJson.put(MessageStrings.FRIEND_ACTION, friendAction.serialized());
        messageJson.put(MessageStrings.SENDER, sender);
        messageJson.put(MessageStrings.RECEIVER, receiver);
        return messageJson;
    }
    public static JSONObject encodeEntryMessage(String refereeKey, String player1Key, String player1ELO, String player2Key, String player2ELO) {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put(MessageStrings.ACTION, MessageStrings.SEND_ENTRY);
        messageJSON.put(JsonStrings.REFEREE_KEY, refereeKey);
        messageJSON.put(JsonStrings.PLAYER_1_KEY, player1Key);
        messageJSON.put(JsonStrings.PLAYER_1_ELO, player1ELO);
        messageJSON.put(JsonStrings.PLAYER_2_KEY, player2Key);
        messageJSON.put(JsonStrings.PLAYER_2_ELO, player2Key);
        return messageJSON;
    }


}
