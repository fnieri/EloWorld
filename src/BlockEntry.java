public class BlockEntry {
    int timestamp;
    Referee referee;
    User[] players;

    User entrySigner;
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

    private String getSignatureString(User entrySigner) {
        return "signature:" + entrySigner.getPublicKey() + "\n";
    }
    public String createEntryStrings() {
        return getTimestampString(timestamp) +
                getPlayersString(players) +
                getRefereeString(referee) +
                getSignatureString(entrySigner);
    }
}
