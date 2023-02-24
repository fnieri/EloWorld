import org.json.JSONObject;

public class Referee extends User implements Serializable {

    int score;
    BlockChain blockchain;

    public Referee(int refereeScore) {
        score = refereeScore;
    }

    public int getRefereeScore() {
        return score;
    }

    /**
     * Create an entry for a played game and store it in a json temp file.
     */
    public void createEntry(String player1, String player2, Boolean player1Wins) {
        // fetch the 2 names in DB to make sure they properly exist

        // Check the current leaderboard in the server to get their current elo and calculate their new elo
        // (the function should already be implemented in another class)
    }

    /**
     * Prompts the referee to choose which entries to add to his next block amongst the one stored in the temp file.
     * Then fetches the blockchains/referee scores available and prompts the referee to choose which blockchain to
     * contribute on. Create a copy of the chosen blockchain, overwriting his own, then add his new block to it.
     */
    public void createBlock() {

    }


    @Override
    public JSONObject asJson() {
        JSONObject refereeJson =  new JSONObject();
        refereeJson.put(getPublicKey(), getRefereeScore());
        return refereeJson;
    }

    // TODO cette fonction envoie au superuser une requete de demotion
    public void requestDemotion() {

    }
}
