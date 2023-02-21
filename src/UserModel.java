public class UserModel extends Model {
    private int elo;

    public int getELO() {
        return elo;
    }

    public void setELO(int newELO) {
        elo = newELO;
        notifyObservers();
    }
}
