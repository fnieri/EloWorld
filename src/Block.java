import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Block {
    String id;
    String previousLocation;
    Block previousBlock;
    LocalDate timeStamp;
    List<String> referees = new ArrayList<>();

    float eloPlayer1;
    float eloPlayer2;
    float refereeScore;
    int playerOneKey;
    int playerTwoKey;
    int refereeKey;

    public void parseEntry(String entry){
        //agit un peu comme un constructeur, ou au moins a cette vocation.
        String[] parsedEntry = entry.split(" ");
        eloPlayer1 = Float.parseFloat(parsedEntry[0]);
        eloPlayer2 = Float.parseFloat(parsedEntry[1]);
        refereeScore = Float.parseFloat(parsedEntry[2]);
        playerOneKey = Integer.parseInt(parsedEntry[3]);
        playerTwoKey = Integer.parseInt(parsedEntry[4]);
        refereeKey = Integer.parseInt(parsedEntry[5]);

        //partie pour le timeStamp :
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        timeStamp = LocalDate.parse(parsedEntry[6], formatter);

        if (referees.contains(Integer.toString(refereeKey))){
            referees.add(Integer.toString(refereeKey));
        }
    }

    public String createEntry(float eloPlayer1, float eloPlayer2, float refereeScore,
                              int playerOneKey, int playerTwoKey, int refereeKey,
                              LocalDate timeStamp){
        //Ca servira au cas ou, c'est une fonction qui créé une String d'initialisation de block
        String newEntry = "";
        newEntry += Float.toString(eloPlayer1);
        newEntry += Float.toString(eloPlayer2);
        newEntry += Float.toString(refereeScore);
        newEntry += Integer.toString(playerOneKey);
        newEntry += Integer.toString(playerTwoKey);
        newEntry += Integer.toString(refereeKey);

        //partie reservée au timeStamp :
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        newEntry += timeStamp.format(formatter);

        return newEntry;
    }

    public int blocScore(){
        return referees.size();
    }



    public void retrieveAllScore(String player){}

    public void retrieveClubScore(String player, String club){}

    public void retrieveLeaderbord(String club){}

    public void retrieveFriends(String player){}

}
