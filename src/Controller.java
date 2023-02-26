import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Enum.*;

import java.util.ArrayList;
import java.util.List;
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
        else if (Objects.equals(domain, Domain.RESOURCE.serialized())) {processSetUp(jsonReq);}
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


    public void processSetUp(JSONObject jsonReq) {
        String username = jsonReq.getString(MessageStrings.USERNAME);
        JSONArray friends = jsonReq.getJSONArray(MessageStrings.FRIEND);

        List<String> friendsList = new ArrayList<>();
        for (int i = 0; i < friends.length(); i ++) {
            String friend = friends.getJSONObject(i).getString(MessageStrings.FRIEND);
            friendsList.add(friend);
        }

        String memberSince = jsonReq.getString(MessageStrings.MEMBER_SINCE);
        int elo = jsonReq.getInt(MessageStrings.ELO);
        int refereeScore = jsonReq.getInt(MessageStrings.REFEREE_SCORE);
        String serializedRole = jsonReq.getString(MessageStrings.ROLE);

        UserRoles role = null;
        if (Objects.equals(serializedRole, UserRoles.USER.serialized())) { role = UserRoles.USER;}
        else if (Objects.equals(serializedRole, UserRoles.REFEREE.serialized())) { role = UserRoles.REFEREE;}
        else if (Objects.equals(serializedRole, UserRoles.SUPER_USER.serialized())) { role = UserRoles.SUPER_USER;}

        model.setUp(memberSince, friendsList, role, elo, refereeScore);

    }
}
