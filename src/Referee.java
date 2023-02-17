import java.time.LocalTime;

public class Referee extends User{

    int score;

    public Referee(int refereeScore) {
        score = refereeScore;
    }

    public int getRefereeScore() {
        return score;
    }


}
