public class BlockChain {
    Block lastBlock;
    String lastblockId;

    public int getScore(Block currentBlock, int currentScore){
        currentScore += currentBlock.blocScore();
        if (currentBlock.getPreviousBlock() != null){
            return getScore(currentBlock.getPreviousBlock(), currentScore);
        } else {
            return currentScore;
        }
    }

    public void getLeaderboard(String club){}

    public void getAllScore(String player){}

    public void getClubScore(String player, String club){}

    public void getFriend(String player){}

}
