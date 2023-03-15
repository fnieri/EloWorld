package src;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        boolean flagConnection = Driver.connection();

        if (flagConnection) {
            String theoDate = getMemberDate("Elliot");
            System.out.println(theoDate);
        }
    }

    public static boolean connection(){
        String url = "jdbc:mysql://ba8e1fd7c94605:443cb91d@eu-cdbr-west-03.cleardb.net/heroku_76cef2360ddfe66?reconnect=true";
        String user = "ba8e1fd7c94605";
        String password = "443cb91d";
        boolean res = false;

        try { // if the connection failed

            connection = DriverManager.getConnection(url, user, password);
            res = true;

        }

        catch (Exception e){
            e.printStackTrace();
        }
        return res;

    }


    private static String stringToSql(String string) {
        // function to add simple quote to a Java String for MySql strings.
        return "'" + string + "'";
    }


    public static boolean auth(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String authQuery = "SELECT * FROM USERS WHERE USERNAME=" + stringToSql(username) + "AND PASSWORD=" + stringToSql(password);

        ResultSet login = statement.executeQuery(authQuery);

        return login.next();

    }

    public static void updatePassword(String username, String password) throws SQLException{
        Statement statement = connection.createStatement();
        String updatePasswordQuery = "UPDATE users SET password =" + stringToSql(password) + "WHERE username =" + stringToSql(username);
        statement.executeUpdate(updatePasswordQuery);
    }

    public static void updateUsername(String username, int idUser) throws SQLException {
        Statement statement = connection.createStatement();
        String updateUsername = "UPDATE users SET username =" + stringToSql(username) + "WHERE iduser =" + idUser;
        statement.executeUpdate(updateUsername);
    }


    private static int getId(String username) throws SQLException{
        int id=-1;
        Statement statement = connection.createStatement();
        String getIdQuery = "SELECT `users`.`iduser` FROM heroku_76cef2360ddfe66.users WHERE `users`.`username` =" + stringToSql(username) +";";
        ResultSet users = statement.executeQuery(getIdQuery);

        if (users.next()){
            id =  Integer.parseInt(users.getString("iduser"));
        }
        return id;

    }

    private static int getMaxId() throws SQLException{
        int maxId = 0;
        Statement statement = connection.createStatement();
        String getMaxIdQuery = "SELECT MAX(`users`.`iduser`) FROM heroku_76cef2360ddfe66.users";
        ResultSet users = statement.executeQuery(getMaxIdQuery);

        if (users.next()){
            maxId =  Integer.parseInt(users.getString("MAX(`users`.`iduser`)"));
        }
        return maxId;
    }

    private static int assignId() throws SQLException{
        return getMaxId() + 1;
    }

    public static void addUser(String username, String password, String memberDate) throws SQLException, NoSuchAlgorithmException {
        RSAKeyGenerator keysGenerator = new RSAKeyGenerator();
        List<String> keys = keysGenerator.generateKeys();
        String privateKey = keys.get(0);
        String publicKey = keys.get(1);

        if (!nameExists(username)) {
            int newId = assignId();
            System.out.println(username + password + memberDate + publicKey + privateKey);
            Statement statement = connection.createStatement();
            String addUserQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`users` (`iduser`, `username`, `password`, `memberDate`, `publicKey`, `privateKey`) VALUES (" + newId + "," + stringToSql(username) + "," + stringToSql(password) + "," + stringToSql(memberDate) + "," + stringToSql("public") + stringToSql("private") + ")" + ";";
            statement.executeUpdate(addUserQuery);
            addRole(newId);
        }
    }

    public static void removeFriend(String username, String friendName) throws SQLException, SQLException {
        if (nameExists(username)){
            int id = getId(username);
            Statement statement = connection.createStatement();
            String removeFriendQuery = "DELETE FROM `heroku_76cef2360ddfe66`.`friends` WHERE `iduser`=" + id + ";";
            statement.executeUpdate(removeFriendQuery);
        }
    }

    public static void addFriend(String username, String friendName) throws SQLException{
        if (nameExists(friendName)) {
            int id = getId(username);
            Statement statement = connection.createStatement();
            String addFriendQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`friends` (`iduser`, `friendname`) VALUES (" + id + "," + stringToSql(friendName) + ");";
            statement.executeUpdate(addFriendQuery);
        }
    }

    public static List<String> getFriendsList(String username) throws SQLException{
        List<String> friendList = new ArrayList<>();
        int id = getId(username);

        if (nameExists(username)) {

            Statement statement = connection.createStatement();
            String getFriendListQuery = "SELECT `friendname` FROM heroku_76cef2360ddfe66.friends WHERE `iduser` =" + id + ";";
            ResultSet friendListSql = statement.executeQuery(getFriendListQuery);

            int i = 0;

            while (friendListSql.next()) {
                String friendName = friendListSql.getString("friendname");
                friendList.add(i, friendName);
                i++;
            }
        }
        return friendList;
    }

    public static void addRole(int id) throws SQLException{
        // set defaultRole = user
        String defaultRole = "'user'";
        Statement statement = connection.createStatement();
        String addRoleQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`roles` (`id`, `role`) VALUES (" + id + "," + defaultRole + ");";
        statement.executeUpdate(addRoleQuery);
    }

    public static void updateRole (int id) throws SQLException{
        // update user -> referee
        String updateRole = "'referee'";
        Statement statement = connection.createStatement();
        String updateRoleQuery = "UPDATE `heroku_76cef2360ddfe66`.`roles` SET `role` =" + updateRole + "WHERE `id` =" + id + ";";
        statement.executeUpdate(updateRoleQuery);
    }

    public static String getRole(String username) throws SQLException{
        int id = getId(username);
        String resRole = "";
        if (nameExists(username)){

            Statement statement = connection.createStatement();
            String getRoleQuery = "SELECT `role` FROM heroku_76cef2360ddfe66.roles WHERE `id` =" + id + ";";
            ResultSet role = statement.executeQuery(getRoleQuery);
            if(role.next()){
                resRole = role.getString("role");
            }
        }
        return resRole;
    }

    public static String getPrivateKey(String username) throws SQLException {
        String privateKey = "";
        if (nameExists(username)){
            Statement statement = connection.createStatement();
            String getPrivateKeyQuery = "SELECT users.privateKey FROM heroku_76cef2360ddfe66.users WHERE username =" + stringToSql(username) + ";";
            ResultSet key = statement.executeQuery(getPrivateKeyQuery);

            if (key.next()){
                privateKey = key.getString("publicKey");
            }
        }
        return privateKey;
    }

    public static String getMemberDate(String username) throws SQLException{
        String memberDate = "";
        if (nameExists(username)){
            Statement statement = connection.createStatement();
            String getMemberDateQuery = "SELECT `users`.`memberDate` FROM `heroku_76cef2360ddfe66`.`users` WHERE `username` =" + stringToSql(username) + ";";
            ResultSet date = statement.executeQuery(getMemberDateQuery);
            if (date.next()){
                // date = "XX/XX/XX"
                memberDate = date.getString("memberDate");
            }
        }
        return memberDate;
    }

    public static String getPublicKey(String username) throws SQLException {
        String publicKey = "";
        if (nameExists(username)){
            Statement statement = connection.createStatement();
            String getPublicKeyQuery = "SELECT `users`.`publicKey` FROM `heroku_76cef2360ddfe66`.`users` WHERE `username` =" + stringToSql(username) + ";";
            ResultSet key = statement.executeQuery(getPublicKeyQuery);

            if (key.next()){
                publicKey = key.getString("publicKey");
            }
        }
        return publicKey;
    }
    public static boolean nameExists(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String nameExists = "SELECT * FROM users WHERE username =" + stringToSql(username);
        ResultSet users = statement.executeQuery(nameExists);
        return users.next();
    }

    public static boolean closeConnection(){
        boolean res = false;

        try{
            connection.close();
            res = true;
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

}