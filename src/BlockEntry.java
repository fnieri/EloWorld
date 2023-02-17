public class BlockEntry {
    int timestamp;
    Referee referee;
    User[] players;
    String signature;

    private String getPlayersString(User[] players) {
        StringBuilder usersString = new StringBuilder();
        for (User player: players) {
            usersString.append(player.asString()).append("\n");
        }
        return usersString.toString();
    }

    private String getRefereeString(Referee referee) {
        return referee.asString() + "\n";
    }

    private String getTimestampString(int timestamp) {
        return "timestamp:" + Integer.toString(timestamp) + "\n";
    }

    private String getSignatureString(String signature) {
        return "signature:" + signature + "\n";
    }

    public String createEntryString() {
        return getTimestampString(timestamp) +
                getPlayersString(players) +
                getRefereeString(referee) +
                getSignatureString(signature);
    }
}