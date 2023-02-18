// import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Driver {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/blockchainappdb";
        String user = "root";
        String password = "1234";

        try { // if the connection failed

            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            System.out.println("Table des users:");
            // resultSet is a structure that catch the answers from the database.
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            // .next() is a boolean value. True when there stay one more data to show.
            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }


            System.out.println("Table des scores:");
            ResultSet rs = statement.executeQuery("SELECT * FROM score");

            while (rs.next()){
                System.out.println(rs.getString("username"));
                System.out.println(rs.getString("score"));
            }


        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
