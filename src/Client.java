import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

// Client class
class Client {

    BufferedReader in;
    static User user = null;

    // driver code
    public void main(String[] args) {

        // establish a connection by providing host and port 8080
        try (Socket socket = new Socket("localhost", 8080)) {

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '/') {
                    if (user instanceof Referee) {
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

    public void sendMessage(String message, Socket socket) throws IOException {
        JSONObject jsonObject = new JSONObject(message);
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
        out.println(jsonObject);
    }

    public void getUserInfo(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Enter username");
        Scanner input = new Scanner(System.in);

        user = new User(input.nextLine());
        out.println(user.publicKey);
        String answer = in.readLine();

        while (!answer.equals("Y")) {
            System.out.println("username already taken, please enter another one");
            user.publicKey = input.nextLine();
            out.println(user.publicKey);
            answer = in.readLine();
        }

        System.out.println("Enter password");
        String password = input.nextLine();
        out.println(password);
    }
}