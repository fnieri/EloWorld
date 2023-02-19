import java.io.*;
import java.net.*;
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
                        System.out.printf("Sent from " + user.userName + " : %s\n", commandHandler(line.charAt(1)));
                        out.println("[" + LocalTime.now() + "] : " + commandHandler(line.charAt(1)));
                    } else {
                        // writing the received message from
                        // client
                        System.out.printf("Sent from " + user.userName + " : %s\n", line);
                        out.println("[" + LocalTime.now() + "] : " + line);
                    }
                }
            }
            catch (IOException e) {
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

        String commandHandler(char command){
            if(command == 't'){
                return "Bonjour";
            }
            return "Au revoir";
        }

        public static void loginHandler(PrintWriter out, BufferedReader in) throws IOException {
            user.userName = in.readLine();
            while (!user.userName.equals("Theo")) { //nom déjà utilisé ou non (voir Quieries)
                out.println("N");
                user.userName = in.readLine();
                }
            out.println("Y");
            user.password = in.readLine();
            }
        }
    }