/**
 * Inspired from the Projet d'année MainModel class (from Boris Petrov)
 */

public class Model extends Subject {
    private String username;
    private String memberSince;

    boolean isLoggedIn = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public void logsIn(String username) {
        isLoggedIn = true;
        setUsername(username);
    }

    public void logsOut() {
        isLoggedIn = false;
    }
}

