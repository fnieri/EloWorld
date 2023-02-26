import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

// ClientHandler class
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    ArrayList<ClientHandler> allClients;
    PrintWriter out = null;
    BufferedReader in = null;

    static User user = new User();
    //static Driver driver = new Driver();

    // Constructor
    public ClientHandler(Socket socket, ArrayList<ClientHandler> connectedClients) {
        this.clientSocket = socket;
        this.allClients = connectedClients;
    }

    public void run() {
        try {

            // get the outputstream of client
            out = new PrintWriter(
                    clientSocket.getOutputStream(), true);

            // get the inputstream of client
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));

            loginHandler(out, in);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '/') {
                    commandHandler(line.charAt(1), line);
                } else {
                    // writing the received message from client
                    System.out.printf("Sent from " + user.publicKey + " : %s\n", line);
                    out.println("[" + LocalTime.now() + "] : " + line);
                }
            }
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            allClients.remove(this);
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void commandHandler(char command, String message){
        String[] users = message.split(" "); //(users[0] = la commande cad /i)
        if(command == 'l'){ //accept
            //database.getLeaderboard()
        }
        if(command == 'f'){ //friend
            //database.addFriend(users[1])
            //}
        }
        if(command == 'j'){ //friend
            //    database.friendLeaderBoard()
        }
        if(command == 'e') { //entry
            //if(user.getclass == Referee){
            //    user.createEntry(tout le tintouin, ca devrait etre facilement accessible)
            //}
        }
        if(command == 'a') { // all
            for(ClientHandler cH: allClients) {
                cH.out.println("getElo");
            }
        }
    }

    public static void loginHandler(PrintWriter out, BufferedReader in) throws IOException, SQLException {
        user.publicKey = in.readLine();
        while (user.publicKey.equals("Theo")) { //(driver.nameExists(user.username)) {
            out.println("N");
            user.publicKey = in.readLine();
        }
        out.println("Y");
        String password = in.readLine();

        //driver.addUser(user.publicKey, password);
    }
}