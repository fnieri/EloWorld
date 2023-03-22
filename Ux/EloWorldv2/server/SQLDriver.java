import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SQLDriver {
    private static Connection connection;

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        boolean flagConnection = connection();

        if (flagConnection) {
            System.out.println(getFriendsList("PAPA"));
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

    private static String getId(String username) throws SQLException{
        String id = "";
        Statement statement = connection.createStatement();
        String getIdQuery = "SELECT `users`.`iduser` FROM heroku_76cef2360ddfe66.users WHERE `users`.`username` =" + stringToSql(username) +";";
        ResultSet users = statement.executeQuery(getIdQuery);

        if (users.next()){
            id = users.getString("iduser");
        }
        return id;

    }

    @Contract(" -> new")
    private static @NotNull
    UUID assignId(){
        return java.util.UUID.randomUUID();
    }

    public static void addUser(String username, String password, String memberDate) throws SQLException, NoSuchAlgorithmException {
        RSAKeyGenerator keysGenerator = new RSAKeyGenerator();
        List<String> keys = keysGenerator.generateKeys();

        String privateKey = keys.get(0);
        String publicKey = keys.get(1);

        String modulus = RSAKeyGenerator.getModulus(publicKey);
        String privateExponent = RSAKeyGenerator.getPrivateExponent(privateKey);
        String publicExponent = RSAKeyGenerator.getPublicExponent(publicKey);

        if (!nameExists(username)) {
            UUID newId = assignId();
            System.out.println(newId);
            Statement statement = connection.createStatement();
            String addUserQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`users` (`iduser`, `username`, `password`, `memberDate`, `modulus`, `privateExponent`, `publicExponent`) VALUES (" + stringToSql(String.valueOf(newId)) + "," + stringToSql(username) + "," + stringToSql(password) + "," + stringToSql(memberDate) + "," + stringToSql(modulus) + "," + stringToSql(privateExponent) + "," + stringToSql(publicExponent) + ");";
            statement.executeUpdate(addUserQuery);
            addRole(newId);
        }
    }

    public static void removeFriend(String username, String friendName) throws SQLException {
        List<String> friendsList = getFriendsList(username);

        if (friendsList.contains(friendName)) {
            if (nameExists(username)) {
                String id = getId(username);
                Statement statement = connection.createStatement();
                String removeFriendQuery = "DELETE FROM `heroku_76cef2360ddfe66`.`friends` WHERE `iduser`=" + stringToSql(id) + ";";
                statement.executeUpdate(removeFriendQuery);
            }
        }
    }

    public static void addFriend(String username, String friendName) throws SQLException{
        List<String> friendsList = getFriendsList(username);

        if (nameExists(friendName)) {
            if (!friendsList.contains(friendName)) {
                String id = getId(username);
                Statement statement = connection.createStatement();
                String addFriendQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`friends` (`iduser`, `friendname`) VALUES (" + stringToSql(id) + "," + stringToSql(friendName) + ");";
                statement.executeUpdate(addFriendQuery);
            }
        }
    }

    public static List<String> getFriendsList(String username) throws SQLException{
        List<String> friendsList = new ArrayList<>();
        String id = getId(username);

        if (nameExists(username)) {

            Statement statement = connection.createStatement();
            String getFriendsListQuery = "SELECT `friendname` FROM heroku_76cef2360ddfe66.friends WHERE `iduser` =" + stringToSql(id) + ";";
            ResultSet friendsListSql = statement.executeQuery(getFriendsListQuery);


            while (friendsListSql.next()) {
                String friendName = friendsListSql.getString("friendname");
                friendsList.add(friendName);
            }
        }
        return friendsList;
    }

    public static void addRole(UUID id) throws SQLException{
        // set defaultRole = user
        String defaultRole = "'user'";
        Statement statement = connection.createStatement();
        String addRoleQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`roles` (`id`, `role`) VALUES (" + stringToSql(String.valueOf(id)) + "," + defaultRole + ");";
        statement.executeUpdate(addRoleQuery);
    }

    public static void updateRole (String username) throws SQLException{
        // update user -> referee
        String id = getId(username);
        String updateRole = "'referee'";
        Statement statement = connection.createStatement();
        String updateRoleQuery = "UPDATE `heroku_76cef2360ddfe66`.`roles` SET `role` =" + updateRole + "WHERE `id` =" + stringToSql(id) + ";";
        statement.executeUpdate(updateRoleQuery);
    }

    public static String getRole(String username) throws SQLException{
        String id = getId(username);
        String resRole = "";
        if (nameExists(username)){

            Statement statement = connection.createStatement();
            String getRoleQuery = "SELECT `role` FROM heroku_76cef2360ddfe66.roles WHERE `id` =" + stringToSql(id) + ";";
            ResultSet role = statement.executeQuery(getRoleQuery);

            if(role.next()){
                resRole = role.getString("role");
            }
        }
        return resRole;
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

    public static String getModulus(String username) throws SQLException {
        String modulus = "";
        if (nameExists(username)){
            Statement statement = connection.createStatement();
            String getPublicKeyQuery = "SELECT `users`.`modulus` FROM `heroku_76cef2360ddfe66`.`users` WHERE `username` =" + stringToSql(username) + ";";
            ResultSet key = statement.executeQuery(getPublicKeyQuery);

            if (key.next()){
                modulus = key.getString("modulus");
            }
        }
        return modulus;
    }

    public static String getPublicExponent(String username) throws SQLException {
        String publicExponent = "";

        if (nameExists(username)){
            Statement statement = connection.createStatement();
            String getPublicEponentQuery = "SELECT `users`.`publicExponent` FROM `heroku_76cef2360ddfe66`.`users` WHERE `username` =" + stringToSql(username) + ";";
            ResultSet user = statement.executeQuery(getPublicEponentQuery);

            if (user.next()){
                publicExponent = user.getString("publicExponent");
            }

        }
        return publicExponent;
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