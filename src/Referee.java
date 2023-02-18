import org.json.JSONObject;

public class Referee extends User implements Serializable {

    int score;

    public Referee(int refereeScore) {
        score = refereeScore;
    }

    public int getRefereeScore() {
        return score;
    }

    @Override
    public JSONObject asJson() {
        JSONObject refereeJson =  new JSONObject();
        refereeJson.put(getPublicKey(), getRefereeScore());
        return refereeJson;
    }


}
