import java.io.*;
import java.net.*;
import java.util.*;

// Client class
class Client {

    static User user = new User();

    // driver code
    public static void main(String[] args) {

        // establish a connection by providing host and port 8080
        try (Socket socket = new Socket("localhost", 8080)) {

            // writing to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            getUserInfo(out, in);

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;

            out.println(user.userName);
            out.println(user.password);

            while (!"exit".equalsIgnoreCase(line)) {

                // reading from user
                line = sc.nextLine();

                // sending the user input to server
                out.println(line);

                // displaying server reply
                System.out.println("Server replied " + in.readLine());
            }

            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getUserInfo(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Enter username");
        Scanner input = new Scanner(System.in);

        user.userName = input.nextLine();
        out.println(user.userName);
        String answer = in.readLine();

        while(!answer.equals("Y")){
            System.out.println("Username already taken, please enter another one");
            user.userName = input.nextLine();
            out.println(user.userName);
            answer = in.readLine();
        }

        System.out.println("Enter password");
        user.password = input.nextLine();
        out.println(user.password);
    }
}