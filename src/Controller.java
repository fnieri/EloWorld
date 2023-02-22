import org.json.JSONException;
import org.json.JSONObject;
import Enum.*;

import java.util.Objects;

public class Controller {
    private Model model;

    public Model getModel() {return new Model();}

    public void parseRequest(String request) {
        JSONObject jsonReq;
        try {
            jsonReq = new JSONObject(request);
        }
        catch (JSONException err) {
            throw new IllegalArgumentException("Wrong request format");
        }

        String domain = jsonReq.getString(MessageStrings.DOMAIN);
        if (Objects.equals(domain, Domain.AUTH.serialized())) {processAuth(jsonReq);}
        else if (Objects.equals(domain, Domain.FRIEND.serialized())) {processFriendReq(jsonReq);}
        else if (Objects.equals(domain, Domain.ENTRY.serialized())) {processEntryCreation(jsonReq);}
    }

    public void processAuth(JSONObject jsonReq) {
        String serverResponse = jsonReq.getString(MessageStrings.SERVER_RESPONSE);
        if (Objects.equals(serverResponse, AuthActions.CLIENT_OK_AUTH.serialized())) {
            String username = jsonReq.getString(MessageStrings.USERNAME);
            String role = jsonReq.getString(MessageStrings.ROLE);
            model.logsIn(username, role);
            //TODO Maybe add elo and member since
        }
        else {
            //TODO Send log in faulty message
        }
    }

    public void processFriendReq(JSONObject jsonReq) {
        String action = jsonReq.getString(MessageStrings.ACTION);
        if (Objects.equals(action, FriendReqActions.SEND_REQUEST.serialized())) {}//TODO serv controller

        else if (Objects.equals(action, FriendReqActions.ACCEPT_REQUEST.serialized())) {
            String newFriend = jsonReq.getString(MessageStrings.SENDER);
            model.addFriend(newFriend);
        }

        else if (Objects.equals(action, FriendReqActions.REMOVE_FRIEND.serialized())) {
            String oldFriend = jsonReq.getString(MessageStrings.SENDER);
            model.removeFriend(oldFriend);
        }
        else {}
    }

    public void processEntryCreation(JSONObject jsonReq) {}
    //TODO send to server
}
