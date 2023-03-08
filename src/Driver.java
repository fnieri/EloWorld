import java.sql.*;

public class Driver {
    private static Connection connection;

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

    public static void updateUsername(String username, Integer userid) throws SQLException {
        Statement statement = connection.createStatement();
        String updateUsername = "UPDATE users SET username =" + stringToSql(username) + "WHERE userid =" + userid;
        statement.executeUpdate(updateUsername);
    }

    public static void addUser(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String addUserQuery = "INSERT INTO `heroku_76cef2360ddfe66`.`users` (`username`, `password`) VALUES (" + stringToSql(username) + ", "+ stringToSql(password) + ")" + ";";
        System.out.println(addUserQuery);
        statement.executeUpdate(addUserQuery);
        addRole(0);

    }

    public static void addRole(Integer id) throws SQLException{
        // set defaultRole = user
        String defaultRole = "user";
        Statement statement = connection.createStatement();
        String addRoleQuery = "";
    }

    public static void updateRole (Integer id) throws SQLException{
        // update user -> referee
        String updateRole = "referee";
        Statement statement = connection.createStatement();
        String updateRoleQuery = "";

    }

    public static Boolean nameExists(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String nameExists = "SELECT * FROM users WHERE username = " + username;
        ResultSet rs = statement.executeQuery(nameExists);
        return !rs.next();
    }
}
