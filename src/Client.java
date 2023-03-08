import java.io.*;
import java.net.*;
import java.util.*;

// Client class
class Client {

    PrintWriter out;
    BufferedReader in;
    static User user = null;

    // driver code
    public void main(String[] args) {

        // establish a connection by providing host and port 8080
        try (Socket socket = new Socket("localhost", 8080)) {

            // writing to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            getUserInfo(this.out, this.in);

            ClientListener listener = new ClientListener(socket);

            new Thread(listener).start();

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String input = null;

            while (!"exit".equalsIgnoreCase(input)) {
                // reading from user
                input = sc.nextLine();

                // sending the user input to server
                out.println(input);
            }

            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo(PrintWriter out, BufferedReader in) throws IOException {
         System.out.println("Enter username");
         Scanner input = new Scanner(System.in);

         user = new User(input.nextLine());
         out.println(user.publicKey);
         String answer = in.readLine();

         while(!answer.equals("Y")){
             System.out.println("username already taken, please enter another one");
             user.publicKey = input.nextLine();
             out.println(user.publicKey);
             answer = in.readLine();
         }

         System.out.println("Enter password");
         String password = input.nextLine();
         out.println(password);
    }



    private static class ClientListener extends Thread {
        private final Socket clientSocket;
        PrintWriter out = null;
        BufferedReader in = null;

        public ClientListener(Socket socket) { this.clientSocket = socket;}
        public void run() {
            try {
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String line;

                while((line = in.readLine()) != null) {
                    if (line.charAt(0) == '/') {
                        if(user instanceof Referee) {
                            System.out.println("Referee");
                            //out.println(user.getLeaderboard);
                        }
                    } else {
                        System.out.print(line + "\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}