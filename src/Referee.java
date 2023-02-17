import java.time.LocalTime;

public class Referee extends User{

    int score = 150;

    public int getRefereeScore() {
        return score;
    }
    public String createEntry(int eloPlayer1, int eloPlayer2,
                              int playerOneKey, int playerTwoKey,
                              LocalTime timeStamp){
        //parsing dans Block.java
        String newEntry = "";
        newEntry += eloPlayer1 + " ";
        newEntry += eloPlayer2 + " ";

        newEntry += playerOneKey + " ";
        newEntry += playerTwoKey + " ";
        newEntry += this.id + " ";

        newEntry += timeStamp;

        return newEntry;
    }
    @Override
    public String asString(){
        return getPublicKey() + ":" + Integer.toString(getRefereeScore());
    }

    public static void main(String[] args){
        Referee ref = new Referee();
        String testEntry = ref.createEntry(720,1200,1515,1212, LocalTime.now());
        System.out.println(testEntry);
    }

}
