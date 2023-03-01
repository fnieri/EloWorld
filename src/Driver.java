import java.sql.*;

public class Driver {
    private static Connection connection;

    public static void main(String[] args) {
        String url = "jdbc:mysql://ba8e1fd7c94605:443cb91d@eu-cdbr-west-03.cleardb.net/heroku_76cef2360ddfe66?reconnect=true";
        String user = "ba8e1fd7c94605";
        String password = "443cb91d";

        try { // if the connection failed

            connection = DriverManager.getConnection(url, user, password);
            //updatePassword("Elliot", "6666");
            //updateUsername("FNIERI", 2);
            //addUser("Nieri", "1111", connection);
            //getScore("Theo", connection);

        }

        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static String stringToSql(String string) {
        // function to add simple quote to a Java String for MySql strings.
        String sqlString = "'" + string + "'";
        return sqlString;
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
        String addUserQuery = "INSERT INTO `blockchainappdb`.`users` (`username`, `password`) VALUES (" + stringToSql(username) + ", "+ stringToSql(password) + ")" + ";";
        System.out.println(addUserQuery);

        try {
            statement.executeUpdate(addUserQuery);
        }

        catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void getScore(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String getScoreQuery = "SELECT score FROM scores WHERE username =" + stringToSql(username);

        ResultSet rs = statement.executeQuery(getScoreQuery);

        while (rs.next()){
            System.out.println(rs.getString("score"));
        }
    }

    public static void deleteScore(String username) throws  SQLException{
        Statement statement = connection.createStatement();
        String deleteScoreQuery = "DELETE FROM scores WHERE username =" + stringToSql(username);

        try {
            statement.executeUpdate(deleteScoreQuery);
        }

        catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void getLeaderboard() throws  SQLException{
        Statement statement = connection.createStatement();
        String getLeaderBoardQuery = "SELECT score FROM scores ORDER BY score LIMIT 10";

        ResultSet rs = statement.executeQuery(getLeaderBoardQuery);

        while (rs.next()){
            System.out.println(rs.getString("score"));
        }
    }
}
