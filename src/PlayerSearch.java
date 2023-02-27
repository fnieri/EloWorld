/**
 * Class to be returned while searching for a player's ELO
 * @param found True if player was found
 * @param ELO   Player's elo, only if found == true
 * @param previousBlock Hash of previous block, only if found == false
 */
public record PlayerSearch(boolean found, int ELO, String previousBlock) {
}