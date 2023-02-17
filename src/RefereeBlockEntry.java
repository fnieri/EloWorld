/**
 * Class representing a block Entry that is entered by a referee when pressing add match in the application
 * Author: Francesco Nieri
 * Date 17/02/2023
 */

public class RefereeBlockEntry implements Serializable {
    int timestamp;
    Referee referee;
    User[] players;
    String signature;

    public RefereeBlockEntry(int timestamp, Referee referee, User[] players, String signature) {
        this.timestamp = timestamp;
        this.referee = referee;
        this.players = players;
        this.signature = signature;
    }
    @Override
    public void asJson(){};
    /*
    private String getPlayersString() {
        StringBuilder usersString = new StringBuilder();
        for (User player: players) {
            usersString.append(player.asString()).append("\n");
        }
        return usersString.toString();
    }

    private String getRefereeString() {
        return referee.asString() + "\n";
    }

    private String getTimestampString() {
        return "timestamp:" + timestamp  + "\n";
    }

    private String getSignatureString() {
        return "signature:" + signature + "\n";
    }

    public String createEntryString() {
        return getTimestampString() +
                getPlayersString() +
                getRefereeString() +
                getSignatureString();
    }

     */
}