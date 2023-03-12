package src;

/**
 * Class to be returned while searching for a player's ELO
*/
public class PlayerSearch {
    private final boolean found;
    private final int ELO;
    private final String previousBlock;

    public PlayerSearch(boolean found, int ELO, String previousBlock) {
        this.found = found;
        this.ELO = ELO;
        this.previousBlock = previousBlock;
    }

    public boolean found() {
        return found;
    }

    public int ELO() {
        return ELO;
    }

    public String previousBlock() {
        return previousBlock;
    }
}