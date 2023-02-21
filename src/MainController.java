import org.json.JSONException;
import org.json.JSONObject;
import Enum.*;

import java.util.Objects;

public class MainController {
    private Model model;

    public Model getModel() {return new Model();}

    public void processRequest(String request) {
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
            model.logsIn(username);
        }
        else {
            //TODO Send log in faulty message
        }
    }

    public void processFriendReq(JSONObject jsonReq) {}

    public void processEntryCreation(JSONObject jsonReq) {}
    //TODO
}
