import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Enum.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Controller {
    private Model model;

    public Controller(Model model) {
        this.model = model;
    }
    public Model getModel() {return model;}

    public void parseRequest(String request) throws JSONException {
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
    }

    public void processAuth(JSONObject jsonReq) throws JSONException {

        String serverResponse = jsonReq.getString(MessageStrings.SERVER_RESPONSE);
        System.out.println(serverResponse);
        System.out.println(src.Enum.AuthActions.CLIENT_OK_AUTH.serialized());
        if (Objects.equals(serverResponse.toLowerCase(Locale.ROOT), AuthActions.CLIENT_OK_AUTH.serialized())) {
            String username = jsonReq.getString(MessageStrings.USERNAME);
            model.logsIn(username);
        }
        else {
            //TODO Send log in faulty message
        }
    }


    public void processSetUp(JSONObject jsonReq) throws JSONException {
        String username = jsonReq.getString(MessageStrings.USERNAME);
        JSONArray friends = jsonReq.getJSONArray(MessageStrings.FRIEND);
        List<String> friendsList = new ArrayList<>();

        for (int i = 0; i < friends.length(); i++) {
            String friend = friends.getJSONObject(i).getString(MessageStrings.FRIEND);
            friendsList.add(friend);
        }

        List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard = new ArrayList<>();
        JSONArray leaderboardArray = jsonReq.getJSONArray(MessageStrings.LEADERBOARD);
        for (int i = 0; i < leaderboardArray.length(); i++) {
            JSONObject entry = leaderboardArray.getJSONObject(i);
            int position = entry.getInt(MessageStrings.POSITION);
            String leaderboardUsername = entry.getString(MessageStrings.USERNAME);
            int ELO = entry.getInt(MessageStrings.ELO);
            Map.Entry<Integer, Map.Entry<String, Integer>> leaderboardListEntry = Map.entry(position, Map.entry(leaderboardUsername, ELO));
            leaderboard.add(leaderboardListEntry);
        }

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

        model.setUp(memberSince, friendsList, role, elo, refereeScore, publicKey, privateKey, leaderboard);
    }
}
