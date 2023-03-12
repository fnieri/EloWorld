package src;

import java.sql.*;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        boolean flagConnection = Driver.connection();

        if (flagConnection){
            addFriend(7, "Elliot");
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

    public static void updateUsername(String username, Integer idUser) throws SQLException {
        Statement statement = connection.createStatement();
        String updateUsername = "UPDATE users SET username =" + stringToSql(username) + "WHERE iduser =" + idUser;
        statement.executeUpdate(updateUsername);
    }


    private static int getId(String username, String password) throws SQLException{
        int id=-1;
        Statement statement = connection.createStatement();
        String getIdQuery = "SELECT `users`.`iduser` FROM heroku_76cef2360ddfe66.users WHERE `users`.`username` =" + stringToSql(username) + "AND `users`.`password`=" + stringToSql(password) + ";";
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

    public static void addUser(String username, String password) throws SQLException {
        int newId = assignId();
        Statement statement = connection.createStatement();
        String addUserQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`users` (`iduser`, `username`, `password`) VALUES ("+ newId + "," + stringToSql(username) + ", "+ stringToSql(password) + ")" + ";";
        statement.executeUpdate(addUserQuery);
        addRole(newId);
    }

    public static void addFriend(Integer id, String friendName) throws SQLException{
        if (!nameExists(friendName)){
            System.out.println("This username don't exists");
        }

        Statement statement = connection.createStatement();
        String addFriendQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`friends` (`iduser`, `friendname`) VALUES (" + id + "," + stringToSql(friendName) +");";
        statement.executeUpdate(addFriendQuery);
    }

    public static void addRole(Integer id) throws SQLException{
        // set defaultRole = user
        String defaultRole = "'user'";
        Statement statement = connection.createStatement();
        String addRoleQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`roles` (`id`, `role`) VALUES (" + id + "," + defaultRole + ");";
        statement.executeUpdate(addRoleQuery);
    }

    public static void updateRole (Integer id) throws SQLException{
        // update user -> referee
        String updateRole = "'referee'";
        Statement statement = connection.createStatement();
        String updateRoleQuery = "UPDATE `heroku_76cef2360ddfe66`.`roles` SET `role` =" + updateRole + "WHERE `id` =" + id + ";";
        statement.executeUpdate(updateRoleQuery);
    }

    public static Boolean nameExists(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String nameExists = "SELECT * FROM users WHERE username =" + stringToSql(username);
        ResultSet users = statement.executeQuery(nameExists);
        return users.next();
    }
}