package src;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

import src.Enum.AuthActions;
import src.Enum.Domain;

// Client class
public class Client {

    PrintWriter out;
    BufferedReader in;
    static User user = null;
    Socket socket;
    Model model = new Model();
    Controller controller = new Controller(model, this);

    public Client() throws IOException {
    }

    // driver code
    public void main() {
        System.out.println("I have been launched");
        // establish a connection by providing host and port 8080
        try  {
            socket = new Socket("10.0.2.2", 8080);

            // writing to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            //getUserInfo(this.out, this.in);

            ClientListener listener = new ClientListener(socket, controller);

            new Thread(listener).start();

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String input = null;

            while (!"exit".equalsIgnoreCase(input)) {
                // reading from user


            }

            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(JSONObject message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            System.out.println("entered sock");
            String messageString = message.toString();
            out.println(messageString);
            out.flush();
            // Reset flag
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Model getModel() {
        return controller.getModel();
    }

    public Controller getController() {
        return controller;
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
        Controller controller;
        private final Socket clientSocket;
        PrintWriter out = null;
        BufferedReader in = null;

        public ClientListener(Socket socket, Controller controller) {
            this.clientSocket = socket;
            this.controller = controller;
        }

        public void run() {
            try {

                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                String packet;

                while((packet = in.readLine()) != null) {

                    parseRequest(packet);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        public void parseRequest(String message) throws JSONException {
            System.out.println(message);
            controller.parseRequest(message);
            }
        }

}