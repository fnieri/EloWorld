package src;

public class SuperUser extends User {

    public SuperUser(String publickey) {
        super(publickey);
    }

    public void promoteUser(String userId){}

    public void demoteReferee(String refId){}

    public void fetchRequests() {}
}
