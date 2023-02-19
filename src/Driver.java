// import java.sql.*;
import java.sql.*;

public class Driver {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/blockchainappdb";
        String user = "root";
        String password = "1234";

        try { // if the connection failed

            Connection connection = DriverManager.getConnection(url, user, password);

            boolean checkFlag = getScore("Elliot", connection);

            if (!checkFlag){
                System.out.println("User is already register");
            }


           //Statement statement = connection.createStatement();

            //System.out.println("Table des users:");
            // resultSet is a structure that catch the answers from the database.
            //String queryUser = "SELECT * FROM users";

            //ResultSet resultSet = statement.executeQuery(queryUser);

            // .next() is a boolean value. True when there stay one more data to show.
            //while (resultSet.next()){
            //    System.out.println(resultSet.getString("password"));
            //}


            //System.out.println("Table des scores:");
            //ResultSet rs = statement.executeQuery("SELECT * FROM score");

            //while (rs.next()){
            //    System.out.println(rs.getString("username"));
            //    System.out.println(rs.getString("score"));
            //}


        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean addUser(String username, String password, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String addUserQuery = "SELECT * FROM users WHERE username='Elliot'";

        ResultSet rs = statement.executeQuery(addUserQuery);

        while (rs.next()){
            System.out.println(rs.getString("username"));
            System.out.println(rs.getString("password"));
        }
        return true;

    }

    public static boolean getScore(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String addUserQuery = "SELECT * FROM score WHERE username="+ username;

        ResultSet rs = statement.executeQuery(addUserQuery);

        while (rs.next()){
            System.out.println(rs.getString("score"));
        }
        return true;

    }
}
