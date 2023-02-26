import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.time.LocalTime;

// Server class
class Server {
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            // server is listening on port 8080
            server = new ServerSocket(8080);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        static User user = new User();
        static Driver driver = new Driver();

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
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
                        System.out.printf("Sent from " + user.username + " : %s\n", commandHandler(line.charAt(1), line));
                        out.println("[" + LocalTime.now() + "] : " + commandHandler(line.charAt(1), line));
                    } else {
                        // writing the received message from
                        // client
                        System.out.printf("Sent from " + user.username + " : %s\n", line);
                        out.println("[" + LocalTime.now() + "] : " + line);
                    }
                }
            }
            catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
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

        String commandHandler(char command, String message){
            String[] users = message.split(" "); //(users[0] = la commande cad /i)
            if(command == 'l'){ //accept
                //database.getLeaderboard()
                return "leaderBoard Global";
            }
            if(command == 'f'){ //friend
                //database.addFriend(users[1])
                //}
                return "invitation envoyée";
            }
            if(command == 'j'){ //friend
            //    database.friendLeaderBoard()
                return "leaderBoard des amis";
            }
            if(command == 'e') { //entry
                //if(user.getclass == Referee){
                //    user.createEntry(tout le tintouin, ca devrait etre facilement accessible)
                //}
                return "entrée générée";
            }
            return "Commande non reconnue";
        }

        public static void loginHandler(PrintWriter out, BufferedReader in) throws IOException, SQLException {
            user.username = in.readLine();
            while (!user.username.equals("Theo")) { //(driver.nameExists(user.username)) {
                out.println("N");
                user.username = in.readLine();
                }
            out.println("Y");
            String password = in.readLine();

            driver.addUser(user.username, password);
            }
        }
    }