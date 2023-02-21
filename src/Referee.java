import java.time.LocalTime;

public class Referee extends User{

    BlockChain chain = new BlockChain();

    public String createEntry(int eloPlayer1, int eloPlayer2,
                              int playerOneKey, int playerTwoKey,
                              LocalTime timeStamp){
        //parsing dans Block.java
        String newEntry = "";
        newEntry += eloPlayer1 + " ";
        newEntry += eloPlayer2 + " ";
        newEntry += this.getScore() + " ";
        newEntry += playerOneKey + " ";
        newEntry += playerTwoKey + " ";

        newEntry += timeStamp;

        return newEntry;
    }

    int getScore() {
        return chain.getScore(chain.lastBlock, 0); //ancienne version de blockchain, a modifier apres
                                                              // le push d'Emile
    }

    public static void main(String[] args){
        Referee ref = new Referee();
        String testEntry = ref.createEntry(720,1200,1515,1212, LocalTime.now());
        System.out.println(testEntry);
    }

}
