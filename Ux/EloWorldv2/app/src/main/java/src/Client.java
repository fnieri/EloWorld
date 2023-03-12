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
        try {
            socket = new Socket("10.0.2.2", 8080);

            // writing to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String packet = null;

            while ((packet = in.readLine()) != null) {
                controller.parseRequest(packet);
            }

            // closing the scanner object
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(JSONObject message) {
        try {
            System.out.println("entered sock");
            String messageString = message.toString();
            out.println(messageString);
            out.flush();
            // Reset flag
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

}